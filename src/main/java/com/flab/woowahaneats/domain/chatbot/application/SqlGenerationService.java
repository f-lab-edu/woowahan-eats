package com.flab.woowahaneats.domain.chatbot.application;

import com.flab.woowahaneats.domain.chatbot.domain.ChatQuestion;
import com.flab.woowahaneats.domain.chatbot.llm.LlmExecutor;
import com.flab.woowahaneats.domain.chatbot.prompt.PromptProvider;
import com.flab.woowahaneats.domain.chatbot.prompt.SchemaLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqlGenerationService {

    private static final String PROMPT_ID = "sql-generation";

    private final SchemaLoader schemaLoader;
    private final PromptProvider promptProvider;
    private final LlmExecutor llmExecutor;

    public String generateSql(ChatQuestion question) {
        String systemPrompt = promptProvider.getSystemPrompt(PROMPT_ID, Map.of(
                "schema", schemaLoader.getSchemaSummary(),
                "restaurantId", question.restaurantId().toString()
        ));

        String userPrompt = promptProvider.getUserPrompt(PROMPT_ID, Map.of(
                "question", question.message()
        ));

        String sql = llmExecutor.call(systemPrompt, userPrompt, promptProvider.getOptions(PROMPT_ID));
        return stripCodeBlock(sql.trim());
    }

    private String stripCodeBlock(String sql) {
        if (sql.startsWith("```")) {
            sql = sql.replaceFirst("```(?:sql)?\\s*\n?", "");
            sql = sql.replaceFirst("\\s*```\\s*$", "");
        }
        sql = sql.replaceAll("--[^\n]*", "");
        sql = sql.replaceAll(";\\s*$", "");
        return sql.trim();
    }
}
