package com.flab.woowahaneats.domain.chatbot.retrieval;

import com.flab.woowahaneats.domain.chatbot.application.ChatbotMessages;
import com.flab.woowahaneats.domain.chatbot.intent.Intent;
import com.flab.woowahaneats.domain.chatbot.query.DynamicQueryExecutor;
import com.flab.woowahaneats.domain.chatbot.query.QueryExecutor;
import com.flab.woowahaneats.domain.chatbot.query.QueryParameters;
import com.flab.woowahaneats.domain.chatbot.query.ParameterExtractor;
import com.flab.woowahaneats.domain.chatbot.query.QueryParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StructuredDataRetriever implements DataRetriever {


    private final ParameterExtractor parameterExtractor;
    private final QueryParameterValidator parameterValidator;
    private final QueryExecutor queryExecutor;
    private final DynamicQueryExecutor dynamicQueryExecutor;

    @Override
    public boolean supports(DataQuery query) {
        return query.intent() != Intent.FAQ_POLICY && query.intent() != Intent.UNSUPPORTED;
    }

    @Override
    public RetrievalResult retrieve(DataQuery query) {
        QueryParameters params = parameterExtractor.extract(query.originalMessage(), query.intent());
        log.info("추출된 파라미터: {}", params);

        // templateName이 null이면 템플릿으로 처리 불가 → 동적 SQL fallback
        if (params.templateName() == null) {
            log.info("템플릿 매칭 불가, 동적 SQL fallback: {}", query.originalMessage());
            return dynamicSqlFallback(query);
        }

        QueryParameters validated = parameterValidator.validateAndFix(params, query.intent());
        if (!params.equals(validated)) {
            log.info("파라미터 보정됨: {}", validated);
        }

        String templateName = validated.templateName() != null ? validated.templateName() : "";

        List<Map<String, Object>> rows = queryExecutor.execute(
                query.intent(), templateName, query.restaurantId(), validated);

        return RetrievalResult.of(formatRows(rows));
    }

    private RetrievalResult dynamicSqlFallback(DataQuery query) {
        return dynamicQueryExecutor.execute(query.originalMessage(), query.restaurantId());
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
