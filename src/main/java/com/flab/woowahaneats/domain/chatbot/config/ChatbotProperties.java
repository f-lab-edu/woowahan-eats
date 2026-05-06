package com.flab.woowahaneats.domain.chatbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "chatbot")
public class ChatbotProperties {

    private final Retrieval retrieval = new Retrieval();
    private final Llm llm = new Llm();
    private final Ingestion ingestion = new Ingestion();

    @Getter
    @Setter
    public static class Retrieval {
        private int topK = 3;
        private double similarityThreshold = 0.5;
    }

    @Getter
    @Setter
    public static class Llm {
        private int maxRetries = 2;
        private long baseDelayMs = 1000;
        private long rateLimitDelayMs = 5000;
    }

    @Getter
    @Setter
    public static class Ingestion {
        private int chunkSize = 400;
        private int chunkOverlap = 50;
    }
}
