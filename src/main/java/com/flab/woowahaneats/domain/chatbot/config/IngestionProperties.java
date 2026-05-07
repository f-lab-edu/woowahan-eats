package com.flab.woowahaneats.domain.chatbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chatbot.ingestion")
public record IngestionProperties(
        int chunkSize,
        int minChunkSizeChars
) {
}
