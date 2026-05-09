package com.flab.woowahaneats.domain.chatbot.exception;

import org.springframework.http.HttpStatus;

public class InvalidCategoryException extends ChatbotException {
    public InvalidCategoryException(String category) {
        super(
                "알 수 없는 카테고리: " + category,
                "INVALID_CATEGORY",
                HttpStatus.BAD_REQUEST
        );
    }
}
