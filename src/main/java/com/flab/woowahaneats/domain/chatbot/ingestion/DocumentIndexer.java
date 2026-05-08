package com.flab.woowahaneats.domain.chatbot.ingestion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentIndexer {

    private static final String METADATA_FILE_PATH = "file_path";
    private static final FilterExpressionBuilder FILTER = new FilterExpressionBuilder();

    private final VectorStore vectorStore;
    private final DocumentIndexMetadataRepository metadataRepository;
    private final TransactionTemplate transactionTemplate;

    public int persist(List<PreparedDocument> prepared, String category) {
        if (prepared.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (PreparedDocument doc : prepared) {
            IngestTarget target = doc.target();

            vectorStore.add(doc.chunks());

            transactionTemplate.executeWithoutResult(status -> {
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
            });

            count++;
        }

        return count;
    }

    public int removeDeletedFiles(String category, Set<String> currentFilePaths) {
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
}
