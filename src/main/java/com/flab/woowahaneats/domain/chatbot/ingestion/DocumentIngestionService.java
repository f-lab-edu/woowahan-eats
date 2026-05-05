package com.flab.woowahaneats.domain.chatbot.ingestion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentIngestionService {

    private static final int CHUNK_SIZE = 400;
    private static final int CHUNK_OVERLAP = 50;

    private final VectorStore vectorStore;

    public void ingest(Resource resource, String category) {
        TextReader reader = new TextReader(resource);
        reader.getCustomMetadata().put("category", category);
        List<Document> documents = reader.read();

        TokenTextSplitter splitter = new TokenTextSplitter(
                CHUNK_SIZE, CHUNK_OVERLAP, 5, 10000, true);
        List<Document> chunks = splitter.split(documents);

        chunks.forEach(chunk ->
                chunk.getMetadata().putAll(Map.of("category", category))
        );

        vectorStore.add(chunks);
        log.info("문서 색인 완료: category={}, chunks={}", category, chunks.size());
    }

    public void ingestAll(List<Resource> resources, String category) {
        resources.forEach(resource -> ingest(resource, category));
    }
}
