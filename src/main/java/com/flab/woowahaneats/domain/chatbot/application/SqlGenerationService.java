package com.flab.woowahaneats.domain.chatbot.application;

import com.flab.woowahaneats.domain.chatbot.domain.ChatQuestion;
import com.flab.woowahaneats.domain.chatbot.exception.LlmCallException;
import com.flab.woowahaneats.domain.chatbot.prompt.PromptProvider;
import com.flab.woowahaneats.domain.chatbot.prompt.SchemaLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqlGenerationService {

    private static final String PROMPT_ID = "sql-generation";

    private final ChatClient chatClient;
    private final SchemaLoader schemaLoader;
    private final PromptProvider promptProvider;

    public String generateSql(ChatQuestion question) {
        try {
            String systemPrompt = promptProvider.getSystemPrompt(PROMPT_ID, Map.of(
                    "schema", schemaLoader.getSchemaSummary(),
                    "restaurantId", question.restaurantId().toString()
            ));

            String userPrompt = promptProvider.getUserPrompt(PROMPT_ID, Map.of(
                    "question", question.message()
            ));

            String sql = chatClient
                    .prompt()
                    .system(systemPrompt)
                    .user(userPrompt)
                    .options(promptProvider.getOptions(PROMPT_ID))
                    .call()
                    .content();

            return stripCodeBlock(sql.trim());
        } catch (Exception e) {
            log.error("SQL 생성 중 LLM 호출 실패: {}", e.getMessage(), e);
            throw new LlmCallException("SQL 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
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
