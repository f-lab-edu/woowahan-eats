package com.flab.woowahaneats.domain.chatbot.query.execution;

import com.flab.woowahaneats.domain.chatbot.application.ChatbotMessages;
import com.flab.woowahaneats.domain.chatbot.query.DynamicQueryExecutor;
import com.flab.woowahaneats.domain.chatbot.query.generation.DynamicSqlGenerator;
import com.flab.woowahaneats.domain.chatbot.query.generation.DynamicSqlGenerator.DynamicSqlResult;
import com.flab.woowahaneats.domain.chatbot.query.validation.SqlValidator;
import com.flab.woowahaneats.domain.chatbot.query.validation.SqlValidator.ValidationResult;
import com.flab.woowahaneats.domain.chatbot.retrieval.RetrievalResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultDynamicQueryExecutor implements DynamicQueryExecutor {

    private final DynamicSqlGenerator dynamicSqlGenerator;
    private final SqlValidator sqlValidator;
    private final SqlExecutor sqlExecutor;

    @Override
    public RetrievalResult execute(String question, Long restaurantId) {
        DynamicSqlResult sqlResult = dynamicSqlGenerator.generate(question, restaurantId);

        if (!sqlResult.success()) {
            log.warn("동적 SQL 생성 실패: {}", sqlResult.errorMessage());
            return RetrievalResult.of(ChatbotMessages.EMPTY_RESULT);
        }

        ValidationResult validation = sqlValidator.validate(sqlResult.sql());
        if (!validation.valid()) {
            log.warn("동적 SQL 검증 실패: {}", validation.errorMessage());
            return RetrievalResult.of(ChatbotMessages.EMPTY_RESULT);
        }

        try {
            List<Map<String, Object>> rows = sqlExecutor.execute(sqlResult.sql(), Map.of());
            log.info("동적 SQL 실행 결과: {}건", rows.size());
            return RetrievalResult.of(formatRows(rows));
        } catch (Exception e) {
            log.warn("동적 SQL 실행 실패: {}", e.getMessage());
            return RetrievalResult.of(ChatbotMessages.EMPTY_RESULT);
        }
    }

    private String formatRows(List<Map<String, Object>> rows) {
        if (rows == null || rows.isEmpty()) {
            return ChatbotMessages.EMPTY_RESULT;
        }
        return rows.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }
}
