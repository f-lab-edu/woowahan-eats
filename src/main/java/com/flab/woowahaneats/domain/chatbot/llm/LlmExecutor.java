package com.flab.woowahaneats.domain.chatbot.llm;

import org.springframework.ai.chat.prompt.ChatOptions;
import reactor.core.publisher.Flux;

public interface LlmExecutor {

    String call(String systemPrompt, String userPrompt, ChatOptions options);

    <T> T call(String systemPrompt, String userPrompt, ChatOptions options, Class<T> responseType);

    Flux<String> stream(String systemPrompt, String userPrompt, ChatOptions options);
}
