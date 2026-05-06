package com.flab.woowahaneats.domain.chatbot.llm;

import com.flab.woowahaneats.domain.chatbot.config.ChatbotProperties;
import com.flab.woowahaneats.domain.chatbot.exception.LlmCallException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResilientLlmExecutor implements LlmExecutor {

    private final ChatClient chatClient;
    private final ChatbotProperties chatbotProperties;

    @Override
    public String call(String systemPrompt, String userPrompt, ChatOptions options) {
        Exception lastException = null;

        ChatbotProperties.Llm llm = chatbotProperties.getLlm();

        for (int attempt = 0; attempt <= llm.getMaxRetries(); attempt++) {
            try {
                if (attempt > 0) {
                    long delay = isRateLimitError(lastException)
                            ? llm.getRateLimitDelayMs()
                            : llm.getBaseDelayMs() * (1L << (attempt - 1));
                    log.info("LLM 호출 재시도 ({}회차), {}ms 대기", attempt, delay);
                    Thread.sleep(delay);
                }

                String content = chatClient
                        .prompt()
                        .system(systemPrompt)
                        .user(userPrompt)
                        .options(options)
                        .call()
                        .content();

                return validateResponse(content);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new LlmCallException("LLM 호출이 중단되었습니다.");
            } catch (LlmCallException e) {
                throw e;
            } catch (Exception e) {
                lastException = e;
                log.warn("LLM 호출 실패 ({}회차): {}", attempt + 1, e.getMessage());
            }
        }

        log.error("LLM 호출 최대 재시도 횟수 초과", lastException);
        throw new LlmCallException("LLM 호출에 실패했습니다. 잠시 후 다시 시도해주세요.");
    }

    private String validateResponse(String content) {
        if (content == null || content.isBlank()) {
            throw new LlmCallException("LLM 응답이 비어있습니다.");
        }
        return content;
    }

    private boolean isRateLimitError(Exception e) {
        if (e == null) return false;
        String message = e.getMessage();
        return message != null && (message.contains("429") || message.contains("rate_limit"));
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
