package com.flab.woowahaneats.domain.chatbot.exception;

import org.springframework.http.HttpStatus;

public class LlmCallException extends ChatbotException {
    public LlmCallException(String message) {
        super(
                message,
                "CHATBOT_LLM_ERROR",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}