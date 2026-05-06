package com.flab.woowahaneats.domain.chatbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chatbot.retrieval")
public record RetrievalProperties(
        int topK,
        double similarityThreshold
) {
}
