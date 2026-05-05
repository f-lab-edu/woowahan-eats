package com.flab.woowahaneats.domain.chatbot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public QueryParameters extract(String message, Intent intent) {
        try {
            String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

            String systemPrompt = promptProvider.getSystemPrompt(PROMPT_ID, Map.of(
                    "today", today,
                    "intent", intent.name()
            ));
            String userPrompt = promptProvider.getUserPrompt(PROMPT_ID, Map.of(
                    "question", message
            ));

            String result = llmExecutor.call(systemPrompt, userPrompt, promptProvider.getOptions(PROMPT_ID));
            return parseResult(result.trim());
        } catch (Exception e) {
            log.warn("파라미터 추출 실패, 기본값 사용: {}", e.getMessage());
            return defaultParameters();
        }
    }

    private QueryParameters parseResult(String json) {
        try {
            String cleaned = json.replaceAll("```(?:json)?\\s*", "").replaceAll("```\\s*$", "").trim();
            JsonNode node = objectMapper.readTree(cleaned);

            return new QueryParameters(
                    getTextOrNull(node, "templateName"),
                    getTextOrNull(node, "startDate"),
                    getTextOrNull(node, "endDate"),
                    getTextOrNull(node, "menuKeyword")
            );
        } catch (Exception e) {
            log.warn("JSON 파싱 실패: {}", e.getMessage());
            return defaultParameters();
        }
    }

    private String getTextOrNull(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value == null || value.isNull()) {
            return null;
        }
        return value.asText();
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
