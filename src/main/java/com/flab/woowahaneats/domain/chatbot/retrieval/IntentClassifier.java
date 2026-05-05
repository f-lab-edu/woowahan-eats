package com.flab.woowahaneats.domain.chatbot.retrieval;

import com.flab.woowahaneats.domain.chatbot.llm.LlmExecutor;
import com.flab.woowahaneats.domain.chatbot.prompt.PromptProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class IntentClassifier {

    private static final String PROMPT_ID = "intent-classifier";

    private final PromptProvider promptProvider;
    private final LlmExecutor llmExecutor;

    public Intent classify(String message) {
        try {
            String systemPrompt = promptProvider.getSystemPrompt(PROMPT_ID, Map.of());

            String result = llmExecutor.call(
                    systemPrompt, message, promptProvider.getOptions(PROMPT_ID));

            return parseIntent(result.trim());
        } catch (IllegalArgumentException e) {
            log.warn("Intent 분류 실패, 기본값 REVENUE 사용: {}", e.getMessage());
            return Intent.REVENUE;
        }
    }

    private Intent parseIntent(String result) {
        String cleaned = result.toUpperCase().replaceAll("[^A-Z_]", "");
        return Intent.valueOf(cleaned);
    }
}
