package com.flab.woowahaneats.domain.chatbot.exception;

import org.springframework.http.HttpStatus;

public class InvalidChatQuestionException extends ChatbotException {
    public InvalidChatQuestionException(String message) {
        super(
                message,
                "CHATBOT_INVALID_QUESTION",
                HttpStatus.BAD_REQUEST
        );
    }
}