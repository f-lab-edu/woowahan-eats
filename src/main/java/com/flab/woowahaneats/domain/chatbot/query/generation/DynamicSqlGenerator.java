package com.flab.woowahaneats.domain.chatbot.query.generation;

import com.flab.woowahaneats.domain.chatbot.llm.LlmExecutor;
import com.flab.woowahaneats.domain.chatbot.prompt.PromptProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicSqlGenerator {

    private static final String PROMPT_ID = "dynamic-sql";

    private final PromptProvider promptProvider;
    private final LlmExecutor llmExecutor;

    public DynamicSqlResult generate(String question, Long restaurantId) {
        try {
            String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);

            String systemPrompt = promptProvider.getSystemPrompt(PROMPT_ID, Map.of(
                    "today", today,
                    "tomorrow", tomorrow
            ));
            String userPrompt = promptProvider.getUserPrompt(PROMPT_ID, Map.of(
                    "question", question
            ));

            String sql = llmExecutor.call(systemPrompt, userPrompt, promptProvider.getOptions(PROMPT_ID));
            String cleaned = cleanSql(sql);

            log.debug("동적 SQL 생성: {}", cleaned);
            return new DynamicSqlResult(cleaned, true, null);
        } catch (Exception e) {
            log.warn("동적 SQL 생성 실패: {}", e.getMessage());
            return new DynamicSqlResult(null, false, e.getMessage());
        }
    }

    private String cleanSql(String sql) {
        return sql.replaceAll("```(?:sql)?\\s*", "")
                .replaceAll("```\\s*$", "")
                .replaceAll(";\\s*$", "")
                .trim();
    }

    public record DynamicSqlResult(String sql, boolean success, String errorMessage) {
    }
}
