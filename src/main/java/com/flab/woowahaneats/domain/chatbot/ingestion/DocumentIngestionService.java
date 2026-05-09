package com.flab.woowahaneats.domain.chatbot.ingestion;

import com.flab.woowahaneats.domain.chatbot.exception.InvalidCategoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentIngestionService {

    private static final Map<String, String> CATEGORY_MAP = Map.of(
            "guide", "서비스 이용 가이드",
            "policy", "정책 및 수수료",
            "faq", "FAQ"
    );

    private final DocumentResourceLoader resourceLoader;
    private final DocumentChunkProcessor chunkProcessor;
    private final DocumentIndexer indexer;
    private final DocumentIndexMetadataRepository metadataRepository;

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
        List<Resource> resources = resourceLoader.loadResources(folder);
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
        List<PreparedDocument> prepared = chunkProcessor.process(targets, category);

        // 3단계: DB + VectorStore 저장 (짧은 트랜잭션)
        int updated = indexer.persist(prepared, category);

        // 4단계: 삭제된 파일 정리 (짧은 트랜잭션)
        int deleted = indexer.removeDeletedFiles(category, currentFilePaths);

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
            String hash = resourceLoader.computeHash(resource);

            Optional<DocumentIndexMetadata> existing = metadataRepository.findByFilePath(filePath);

            if (existing.isPresent() && existing.get().getContentHash().equals(hash)) {
                continue;
            }

            targets.add(new IngestTarget(resource, filePath, hash, existing.orElse(null)));
        }

        return targets;
    }

    public record IngestResult(int updated, int skipped, int deleted) {
    }
}
