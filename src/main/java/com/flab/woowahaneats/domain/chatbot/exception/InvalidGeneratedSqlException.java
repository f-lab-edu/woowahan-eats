package com.flab.woowahaneats.domain.chatbot.exception;

import org.springframework.http.HttpStatus;

public class InvalidGeneratedSqlException extends ChatbotException {
    public InvalidGeneratedSqlException(String message) {
        super(
                message,
                "CHATBOT_INVALID_SQL",
                HttpStatus.BAD_REQUEST
        );
    }
}