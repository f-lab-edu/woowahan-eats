package com.flab.woowahaneats.domain.chatbot.ingestion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentIngestionRunner implements ApplicationRunner {

    private final DocumentIngestionService ingestionService;
    private final VectorStore vectorStore;

    @Override
    public void run(ApplicationArguments args) {
        if (isAlreadyIndexed()) {
            log.info("문서가 이미 색인되어 있습니다. 건너뜁니다.");
            return;
        }

        try {
            ingestCategory("guide", "서비스 이용 가이드");
            ingestCategory("policy", "정책 및 수수료");
            ingestCategory("faq", "FAQ");
            log.info("전체 문서 색인 완료");
        } catch (IOException e) {
            log.error("문서 색인 실패: {}", e.getMessage(), e);
        }
    }

    private void ingestCategory(String folder, String category) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:documents/" + folder + "/*.md");

        if (resources.length == 0) {
            log.warn("색인할 문서 없음: documents/{}/", folder);
            return;
        }

        List<Resource> resourceList = Arrays.asList(resources);
        ingestionService.ingestAll(resourceList, category);
    }

    private boolean isAlreadyIndexed() {
        try {
            List<Document> results = vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query("test")
                            .topK(1)
                            .build()
            );
            return !results.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
