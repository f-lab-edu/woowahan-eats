package com.flab.woowahaneats.domain.chatbot.ingestion;

import com.flab.woowahaneats.domain.chatbot.config.IngestionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DocumentChunkProcessor {

    private static final String METADATA_FILE_PATH = "file_path";

    private final IngestionProperties ingestionProperties;

    public List<PreparedDocument> process(
            List<IngestTarget> targets, String category) {

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
}
