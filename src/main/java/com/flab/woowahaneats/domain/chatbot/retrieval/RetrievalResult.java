package com.flab.woowahaneats.domain.chatbot.retrieval;

public record RetrievalResult(
        String data
) {
    public static RetrievalResult of(String data) {
        return new RetrievalResult(data);
    }
}
