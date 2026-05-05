package com.flab.woowahaneats.domain.chatbot.controller.dto;

public record ChatResponse(
        String answer
) {
    public static ChatResponse of(String answer) {
        return new ChatResponse(answer);
    }
}