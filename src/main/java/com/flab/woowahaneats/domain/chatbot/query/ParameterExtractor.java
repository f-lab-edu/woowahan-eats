package com.flab.woowahaneats.domain.chatbot.query;

import com.flab.woowahaneats.domain.chatbot.llm.LlmExecutor;
import com.flab.woowahaneats.domain.chatbot.prompt.PromptProvider;
import com.flab.woowahaneats.domain.chatbot.retrieval.Intent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParameterExtractor {

    private static final String PROMPT_ID = "parameter-extractor";

    private final PromptProvider promptProvider;
    private final LlmExecutor llmExecutor;

    public QueryParameters extract(String message, Intent intent) {
        try {
            String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);

            String systemPrompt = promptProvider.getSystemPrompt(PROMPT_ID, Map.of(
                    "today", today,
                    "tomorrow", tomorrow,
                    "intent", intent.name()
            ));
            String userPrompt = promptProvider.getUserPrompt(PROMPT_ID, Map.of(
                    "question", message
            ));

            QueryParameters result = llmExecutor.call(
                    systemPrompt, userPrompt, promptProvider.getOptions(PROMPT_ID), QueryParameters.class);

            log.info("추출된 파라미터: {}", result);
            return result;
        } catch (Exception e) {
            log.warn("파라미터 추출 실패, 기본값 사용: {}", e.getMessage());
            return defaultParameters();
        }
    }

    private QueryParameters defaultParameters() {
        LocalDate now = LocalDate.now();
        return new QueryParameters(
                null,
                now.withDayOfMonth(1).format(DateTimeFormatter.ISO_LOCAL_DATE),
                now.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE),
                null
        );
    }
}
