package com.flab.woowahaneats.domain.chatbot.exception;

import org.springframework.http.HttpStatus;

public class SqlExecutionException extends ChatbotException {
    public SqlExecutionException(String message) {
        super(
                message,
                "CHATBOT_SQL_EXECUTION_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}