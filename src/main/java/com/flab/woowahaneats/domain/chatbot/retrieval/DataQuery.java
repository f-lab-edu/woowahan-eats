package com.flab.woowahaneats.domain.chatbot.retrieval;

import com.flab.woowahaneats.domain.chatbot.intent.Intent;

public record DataQuery(
        Intent intent,
        String originalMessage,
        Long restaurantId
) {
}
