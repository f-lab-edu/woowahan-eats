package com.flab.woowahaneats.domain.chatbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chatbot.llm")
public record LlmProperties(
        int maxRetries,
        long baseDelayMs,
        long rateLimitDelayMs
) {
}
