package com.flab.woowahaneats.domain.chatbot.controller.dto;

public record ChatResponse(
        String answer,
        String sqlUsed
) {
    public static ChatResponse of(String answer, String sqlUsed) {
        return new ChatResponse(answer, sqlUsed);
    }
}