package com.flab.woowahaneats.domain.chatbot.llm;

import org.springframework.ai.chat.prompt.ChatOptions;
import reactor.core.publisher.Flux;

public interface LlmExecutor {

    String call(String systemPrompt, String userPrompt, ChatOptions options);

    Flux<String> stream(String systemPrompt, String userPrompt, ChatOptions options);
}
