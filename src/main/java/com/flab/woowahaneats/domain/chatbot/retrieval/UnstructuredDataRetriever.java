package com.flab.woowahaneats.domain.chatbot.retrieval;

import com.flab.woowahaneats.domain.chatbot.config.ChatbotProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnstructuredDataRetriever implements DataRetriever {

    private final VectorStore vectorStore;
    private final ChatbotProperties chatbotProperties;

    @Override
    public boolean supports(DataQuery query) {
        return query.intent() == Intent.FAQ_POLICY;
    }

    @Override
    public RetrievalResult retrieve(DataQuery query) {
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query.originalMessage())
                        .topK(chatbotProperties.getRetrieval().getTopK())
                        .similarityThreshold(chatbotProperties.getRetrieval().getSimilarityThreshold())
                        .build()
        );

        if (documents.isEmpty()) {
            log.info("관련 문서를 찾지 못했습니다: {}", query.originalMessage());
            return RetrievalResult.of("(관련 정보를 찾지 못했습니다)");
        }

        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        log.info("검색된 문서 {}건, 질문: {}", documents.size(), query.originalMessage());
        return RetrievalResult.of(context);
    }
}
