package com.flab.woowahaneats.domain.chatbot.ingestion;

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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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

    private static final int CHUNK_SIZE = 400;
    private static final int CHUNK_OVERLAP = 50;
    private static final Map<String, String> CATEGORY_MAP = Map.of(
            "guide", "서비스 이용 가이드",
            "policy", "정책 및 수수료",
            "faq", "FAQ"
    );

    private static final FilterExpressionBuilder FILTER = new FilterExpressionBuilder();

    private final VectorStore vectorStore;
    private final DocumentIndexMetadataRepository metadataRepository;

    public IngestResult ingestAll() {
        int updated = 0;
        int skipped = 0;
        int deleted = 0;

        for (Map.Entry<String, String> entry : CATEGORY_MAP.entrySet()) {
            IngestResult result = ingestCategory(entry.getKey(), entry.getValue());
            updated += result.updated();
            skipped += result.skipped();
            deleted += result.deleted();
        }

        return new IngestResult(updated, skipped, deleted);
    }

    @Transactional
    public IngestResult ingestCategory(String folder, String category) {
        List<Resource> resources = loadResources(folder);
        if (resources.isEmpty()) {
            log.warn("색인할 문서 없음: documents/{}/", folder);
            return new IngestResult(0, 0, 0);
        }

        int updated = 0;
        int skipped = 0;

        Set<String> currentFilePaths = resources.stream()
                .map(r -> folder + "/" + r.getFilename())
                .collect(Collectors.toSet());

        for (Resource resource : resources) {
            String filePath = folder + "/" + resource.getFilename();
            String hash = computeHash(resource);

            Optional<DocumentIndexMetadata> existing = metadataRepository.findByFilePath(filePath);

            if (existing.isPresent() && existing.get().getContentHash().equals(hash)) {
                skipped++;
                continue;
            }

            if (existing.isPresent()) {
                vectorStore.delete(FILTER.eq("file_path", filePath).build());
                existing.get().updateHash(hash);
            } else {
                metadataRepository.save(DocumentIndexMetadata.builder()
                        .filePath(filePath)
                        .category(category)
                        .contentHash(hash)
                        .indexedAt(LocalDateTime.now())
                        .build());
            }

            indexDocument(resource, category, filePath);
            updated++;
        }

        int deleted = removeDeletedFiles(category, currentFilePaths);

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

    private void indexDocument(Resource resource, String category, String filePath) {
        TextReader reader = new TextReader(resource);
        reader.getCustomMetadata().put("category", category);
        reader.getCustomMetadata().put("file_path", filePath);
        List<Document> documents = reader.read();

        TokenTextSplitter splitter = new TokenTextSplitter(
                CHUNK_SIZE, CHUNK_OVERLAP, 5, 10000, true);
        List<Document> chunks = splitter.split(documents);

        chunks.forEach(chunk ->
                chunk.getMetadata().putAll(Map.of("category", category, "file_path", filePath))
        );

        vectorStore.add(chunks);
    }

    private int removeDeletedFiles(String category, Set<String> currentFilePaths) {
        List<DocumentIndexMetadata> indexed = metadataRepository.findAllByCategory(category);
        int deleted = 0;

        for (DocumentIndexMetadata metadata : indexed) {
            if (!currentFilePaths.contains(metadata.getFilePath())) {
                vectorStore.delete(FILTER.eq("file_path", metadata.getFilePath()).build());
                metadataRepository.delete(metadata);
                deleted++;
                log.info("삭제된 문서 정리: {}", metadata.getFilePath());
            }
        }

        return deleted;
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
}
