package com.flab.woowahaneats.domain.chatbot.query;

public record QueryParameters(
        String templateName,
        String startDate,
        String endDate,
        String menuKeyword
) {
}
