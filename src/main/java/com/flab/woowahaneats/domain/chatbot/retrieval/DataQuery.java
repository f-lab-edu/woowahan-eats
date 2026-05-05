package com.flab.woowahaneats.domain.chatbot.retrieval;

public record DataQuery(
        Intent intent,
        String originalMessage,
        Long restaurantId
) {
}
