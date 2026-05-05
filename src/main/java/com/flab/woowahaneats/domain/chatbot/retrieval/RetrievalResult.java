package com.flab.woowahaneats.domain.chatbot.retrieval;

public record RetrievalResult(
        String data,
        String source
) {
    public static RetrievalResult of(String data, String source) {
        return new RetrievalResult(data, source);
    }
}
