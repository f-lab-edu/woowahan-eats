package com.flab.woowahaneats.domain.chatbot.domain;

import com.flab.woowahaneats.domain.chatbot.exception.InvalidChatQuestionException;

public record ChatQuestion(Long restaurantId, String message) {

    private static final int MAX_MESSAGE_LENGTH = 500;

    public ChatQuestion {
        if (restaurantId == null) {
            throw new InvalidChatQuestionException("음식점 ID는 필수입니다.");
        }
        if (message == null || message.isBlank()) {
            throw new InvalidChatQuestionException("질문을 입력해주세요.");
        }
        if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new InvalidChatQuestionException(
                    "질문은 " + MAX_MESSAGE_LENGTH + "자 이내로 입력해주세요."
            );
        }
    }
}