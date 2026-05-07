package com.flab.woowahaneats.domain.chatbot.retrieval;

import com.flab.woowahaneats.domain.chatbot.query.DynamicSqlGenerator;
import com.flab.woowahaneats.domain.chatbot.query.DynamicSqlGenerator.DynamicSqlResult;
import com.flab.woowahaneats.domain.chatbot.query.ParameterExtractor;
import com.flab.woowahaneats.domain.chatbot.query.QueryExecutor;
import com.flab.woowahaneats.domain.chatbot.query.QueryParameterValidator;
import com.flab.woowahaneats.domain.chatbot.query.QueryParameters;
import com.flab.woowahaneats.domain.chatbot.query.SqlExecutor;
import com.flab.woowahaneats.domain.chatbot.query.SqlValidator;
import com.flab.woowahaneats.domain.chatbot.query.SqlValidator.ValidationResult;
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

    private static final String EMPTY_RESULT = "(결과 없음)";

    private final ParameterExtractor parameterExtractor;
    private final QueryParameterValidator parameterValidator;
    private final QueryExecutor queryExecutor;
    private final DynamicSqlGenerator dynamicSqlGenerator;
    private final SqlValidator sqlValidator;
    private final SqlExecutor sqlExecutor;

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

        if (!rows.isEmpty()) {
            return RetrievalResult.of(formatRows(rows));
        }

        // 템플릿 결과가 비어있어도 동적 SQL fallback
        log.info("템플릿 결과 없음, 동적 SQL fallback 시도: {}", query.originalMessage());
        return dynamicSqlFallback(query);
    }

    private RetrievalResult dynamicSqlFallback(DataQuery query) {
        DynamicSqlResult sqlResult = dynamicSqlGenerator.generate(
                query.originalMessage(), query.restaurantId());

        if (!sqlResult.success()) {
            log.warn("동적 SQL 생성 실패: {}", sqlResult.errorMessage());
            return RetrievalResult.of(EMPTY_RESULT);
        }

        ValidationResult validation = sqlValidator.validate(sqlResult.sql());
        if (!validation.valid()) {
            log.warn("동적 SQL 검증 실패: {}", validation.errorMessage());
            return RetrievalResult.of(EMPTY_RESULT);
        }

        try {
            List<Map<String, Object>> rows = sqlExecutor.execute(sqlResult.sql(), Map.of());
            log.info("동적 SQL 실행 결과: {}건", rows.size());
            return RetrievalResult.of(formatRows(rows));
        } catch (Exception e) {
            log.warn("동적 SQL 실행 실패: {}", e.getMessage());
            return RetrievalResult.of(EMPTY_RESULT);
        }
    }

    private String formatRows(List<Map<String, Object>> rows) {
        if (rows == null || rows.isEmpty()) {
            return EMPTY_RESULT;
        }
        return rows.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }
}
