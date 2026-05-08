package com.flab.woowahaneats.domain.chatbot.llm;

import com.flab.woowahaneats.domain.chatbot.exception.LlmCallException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResilientLlmExecutor implements LlmExecutor {

    private static final String RETRY_LOG_FORMAT = "LLM 호출 실패 (재시도 예정): {}";

    private final ChatClient chatClient;

    @Override
    @Retryable(retryFor = LlmCallException.class,
            maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public String call(String systemPrompt, String userPrompt, ChatOptions options) {
        try {
            String content = chatClient
                    .prompt()
                    .system(systemPrompt)
                    .user(userPrompt)
                    .options(options)
                    .call()
                    .content();

            if (content == null || content.isBlank()) {
                throw new LlmCallException("LLM 응답이 비어있습니다.");
            }
            return content;
        } catch (LlmCallException e) {
            log.warn(RETRY_LOG_FORMAT, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.warn(RETRY_LOG_FORMAT, e.getMessage());
            throw new LlmCallException("LLM 호출에 실패했습니다.");
        }
    }

    @Override
    @Retryable(retryFor = LlmCallException.class,
            maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public <T> T call(String systemPrompt, String userPrompt, ChatOptions options, Class<T> responseType) {
        try {
            return chatClient
                    .prompt()
                    .system(systemPrompt)
                    .user(userPrompt)
                    .options(options)
                    .call()
                    .entity(responseType);
        } catch (Exception e) {
            log.warn(RETRY_LOG_FORMAT, e.getMessage());
            throw new LlmCallException("LLM 호출에 실패했습니다.");
        }
    }

    @Override
    public Flux<String> stream(String systemPrompt, String userPrompt, ChatOptions options) {
        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .options(options)
                .stream()
                .content()
                .onErrorResume(e -> {
                    log.error("LLM 스트리밍 호출 실패: {}", e.getMessage(), e);
                    return Flux.error(new LlmCallException(
                            "LLM 호출에 실패했습니다. 잠시 후 다시 시도해주세요."));
                });
    }
}
