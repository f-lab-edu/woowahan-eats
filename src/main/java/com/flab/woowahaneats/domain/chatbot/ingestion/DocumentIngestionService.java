package com.flab.woowahaneats.domain.chatbot.ingestion;

import com.flab.woowahaneats.domain.chatbot.config.IngestionProperties;
import com.flab.woowahaneats.domain.chatbot.exception.InvalidCategoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentIngestionService {

    private static final String METADATA_FILE_PATH = "file_path";
    private static final Map<String, String> CATEGORY_MAP = Map.of(
            "guide", "서비스 이용 가이드",
            "policy", "정책 및 수수료",
            "faq", "FAQ"
    );

    private static final FilterExpressionBuilder FILTER = new FilterExpressionBuilder();

    private final VectorStore vectorStore;
    private final DocumentIndexMetadataRepository metadataRepository;
    private final TransactionTemplate transactionTemplate;
    private final IngestionProperties ingestionProperties;

    public IngestResult ingestAll() {
        int updated = 0;
        int skipped = 0;
        int deleted = 0;

        for (Map.Entry<String, String> entry : CATEGORY_MAP.entrySet()) {
            try {
                IngestResult result = ingestCategory(entry.getKey(), entry.getValue());
                updated += result.updated();
                skipped += result.skipped();
                deleted += result.deleted();
            } catch (Exception e) {
                log.error("카테고리 색인 실패: {}", entry.getValue(), e);
            }
        }

        return new IngestResult(updated, skipped, deleted);
    }

    public IngestResult ingestCategory(String folder, String category) {
        List<Resource> resources = loadResources(folder);
        if (resources.isEmpty()) {
            log.warn("색인할 문서 없음: documents/{}/", folder);
            return new IngestResult(0, 0, 0);
        }

        Set<String> currentFilePaths = resources.stream()
                .map(r -> folder + "/" + r.getFilename())
                .collect(Collectors.toSet());

        // 1단계: 변경 감지 (트랜잭션 불필요)
        List<IngestTarget> targets = detectChanges(resources, folder);

        // 2단계: 임베딩용 청크 생성 (트랜잭션 밖 — 문서 파싱/분할만, 임베딩은 add 시점에 수행)
        List<PreparedDocument> prepared = prepareDocuments(targets, category);

        // 3단계: DB + VectorStore 저장 (짧은 트랜잭션)
        int updated = persist(prepared, category);

        // 4단계: 삭제된 파일 정리 (짧은 트랜잭션)
        int deleted = removeDeletedFiles(category, currentFilePaths);

        int skipped = resources.size() - targets.size();

        log.info("색인 완료: category={}, updated={}, skipped={}, deleted={}",
                category, updated, skipped, deleted);
        return new IngestResult(updated, skipped, deleted);
    }

    public String resolveCategory(String folder) {
        String category = CATEGORY_MAP.get(folder);
        if (category == null) {
            throw new InvalidCategoryException(folder);
        }
        return category;
    }

    private List<IngestTarget> detectChanges(List<Resource> resources, String folder) {
        List<IngestTarget> targets = new ArrayList<>();

        for (Resource resource : resources) {
            String filePath = folder + "/" + resource.getFilename();
            String hash = computeHash(resource);

            Optional<DocumentIndexMetadata> existing = metadataRepository.findByFilePath(filePath);

            if (existing.isPresent() && existing.get().getContentHash().equals(hash)) {
                continue;
            }

            targets.add(new IngestTarget(resource, filePath, hash, existing.orElse(null)));
        }

        return targets;
    }

    private List<PreparedDocument> prepareDocuments(List<IngestTarget> targets, String category) {
        List<PreparedDocument> prepared = new ArrayList<>();

        for (IngestTarget target : targets) {
            TextReader reader = new TextReader(target.resource());
            reader.getCustomMetadata().put("category", category);
            reader.getCustomMetadata().put(METADATA_FILE_PATH, target.filePath());
            List<Document> documents = reader.read();

            TokenTextSplitter splitter = TokenTextSplitter.builder()
                    .withChunkSize(ingestionProperties.chunkSize())
                    .withMinChunkSizeChars(ingestionProperties.minChunkSizeChars())
                    .build();
            List<Document> chunks = splitter.split(documents);

            chunks.forEach(chunk ->
                    chunk.getMetadata().putAll(Map.of("category", category, METADATA_FILE_PATH, target.filePath()))
            );

            prepared.add(new PreparedDocument(target, chunks));
        }

        return prepared;
    }

    private int persist(List<PreparedDocument> prepared, String category) {
        if (prepared.isEmpty()) {
            return 0;
        }

        // 1) DB 메타데이터 저장 + 기존 벡터 삭제 (짧은 트랜잭션)
        transactionTemplate.executeWithoutResult(status -> {
            for (PreparedDocument doc : prepared) {
                IngestTarget target = doc.target();

                if (target.existingMetadata() != null) {
                    vectorStore.delete(FILTER.eq(METADATA_FILE_PATH, target.filePath()).build());
                    target.existingMetadata().updateHash(target.hash());
                } else {
                    metadataRepository.save(DocumentIndexMetadata.builder()
                            .filePath(target.filePath())
                            .category(category)
                            .contentHash(target.hash())
                            .indexedAt(LocalDateTime.now())
                            .build());
                }
            }
        });

        // 2) VectorStore 임베딩 + 저장 (트랜잭션 밖 — 임베딩 HTTP 호출 포함)
        int count = 0;
        for (PreparedDocument doc : prepared) {
            vectorStore.add(doc.chunks());
            count++;
        }

        return count;
    }

    private int removeDeletedFiles(String category, Set<String> currentFilePaths) {
        Integer deleted = transactionTemplate.execute(status -> {
            List<DocumentIndexMetadata> indexed = metadataRepository.findAllByCategory(category);
            int count = 0;

            for (DocumentIndexMetadata metadata : indexed) {
                if (!currentFilePaths.contains(metadata.getFilePath())) {
                    vectorStore.delete(FILTER.eq(METADATA_FILE_PATH, metadata.getFilePath()).build());
                    metadataRepository.delete(metadata);
                    count++;
                    log.info("삭제된 문서 정리: {}", metadata.getFilePath());
                }
            }

            return count;
        });

        return deleted != null ? deleted : 0;
    }

    private List<Resource> loadResources(String folder) {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:documents/" + folder + "/*.md");
            return Arrays.asList(resources);
        } catch (IOException e) {
            log.error("문서 로드 실패: documents/{}/", folder, e);
            return List.of();
        }
    }

    private String computeHash(Resource resource) {
        try (InputStream is = resource.getInputStream()) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(is.readAllBytes());
            return HexFormat.of().formatHex(hash);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("해시 계산 실패: " + resource.getFilename(), e);
        }
    }

    public record IngestResult(int updated, int skipped, int deleted) {
    }

    private record IngestTarget(
            Resource resource,
            String filePath,
            String hash,
            DocumentIndexMetadata existingMetadata
    ) {
    }

    private record PreparedDocument(
            IngestTarget target,
            List<Document> chunks
    ) {
    }
}
