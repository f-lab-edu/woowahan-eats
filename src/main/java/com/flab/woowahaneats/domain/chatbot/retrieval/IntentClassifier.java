package com.flab.woowahaneats.domain.chatbot.retrieval;

import com.flab.woowahaneats.domain.chatbot.exception.LlmCallException;
import com.flab.woowahaneats.domain.chatbot.prompt.PromptProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class IntentClassifier {

    private static final String PROMPT_ID = "intent-classifier";

    private final ChatClient chatClient;
    private final PromptProvider promptProvider;

    public Intent classify(String message) {
        try {
            String systemPrompt = promptProvider.getSystemPrompt(PROMPT_ID, Map.of());

            String result = chatClient
                    .prompt()
                    .system(systemPrompt)
                    .user(message)
                    .options(promptProvider.getOptions(PROMPT_ID))
                    .call()
                    .content();

            return parseIntent(result.trim());
        } catch (IllegalArgumentException e) {
            log.warn("Intent 분류 실패, 기본값 REVENUE 사용: {}", e.getMessage());
            return Intent.REVENUE;
        } catch (Exception e) {
            log.error("Intent 분류 중 LLM 호출 실패: {}", e.getMessage(), e);
            throw new LlmCallException("질문 분류 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private Intent parseIntent(String result) {
        String cleaned = result.toUpperCase().replaceAll("[^A-Z_]", "");
        return Intent.valueOf(cleaned);
    }
}
