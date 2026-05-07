package com.flab.woowahaneats.domain.chatbot.retrieval;

import com.flab.woowahaneats.domain.chatbot.query.DynamicSqlGenerator;
import com.flab.woowahaneats.domain.chatbot.query.DynamicSqlGenerator.DynamicSqlResult;
import com.flab.woowahaneats.domain.chatbot.query.ParameterExtractor;
import com.flab.woowahaneats.domain.chatbot.query.QueryExecutor;
import com.flab.woowahaneats.domain.chatbot.query.QueryParameters;
import com.flab.woowahaneats.domain.chatbot.query.QueryTemplateRegistry;
import com.flab.woowahaneats.domain.chatbot.query.SqlExecutor;
import com.flab.woowahaneats.domain.chatbot.query.SqlValidator;
import com.flab.woowahaneats.domain.chatbot.query.SqlValidator.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StructuredDataRetriever implements DataRetriever {

    private final ParameterExtractor parameterExtractor;
    private final QueryExecutor queryExecutor;
    private final QueryTemplateRegistry templateRegistry;
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

        QueryParameters validated = validateAndFix(params, query.intent());
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
            return RetrievalResult.of("(결과 없음)");
        }

        ValidationResult validation = sqlValidator.validate(sqlResult.sql());
        if (!validation.valid()) {
            log.warn("동적 SQL 검증 실패: {}", validation.errorMessage());
            return RetrievalResult.of("(결과 없음)");
        }

        try {
            List<Map<String, Object>> rows = sqlExecutor.execute(sqlResult.sql(), Map.of());
            log.info("동적 SQL 실행 결과: {}건", rows.size());
            return RetrievalResult.of(formatRows(rows));
        } catch (Exception e) {
            log.warn("동적 SQL 실행 실패: {}", e.getMessage());
            return RetrievalResult.of("(결과 없음)");
        }
    }

    private QueryParameters validateAndFix(QueryParameters params, Intent intent) {
        String templateName = params.templateName();
        String startDate = params.startDate();
        String endDate = params.endDate();
        String menuKeyword = params.menuKeyword();

        if (templateName != null && !templateRegistry.hasTemplate(intent, templateName)) {
            log.warn("템플릿 '{}' 은 intent {} 에 없음, 기본 템플릿으로 보정", templateName, intent);
            templateName = null;
        }

        startDate = validateDate(startDate, LocalDate.now().withDayOfMonth(1));
        endDate = validateDate(endDate, LocalDate.now().plusDays(1));

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if (start.isAfter(end) || start.isEqual(end)) {
            log.warn("날짜 범위 비정상: {} ~ {}, 이번 달로 보정", startDate, endDate);
            startDate = LocalDate.now().withDayOfMonth(1).toString();
            endDate = LocalDate.now().plusDays(1).toString();
        }

        return new QueryParameters(templateName, startDate, endDate, menuKeyword);
    }

    private String validateDate(String date, LocalDate fallback) {
        if (date == null || date.isBlank()) {
            return fallback.toString();
        }
        try {
            LocalDate.parse(date);
            return date;
        } catch (DateTimeParseException e) {
            log.warn("날짜 파싱 실패: '{}', 기본값 {} 사용", date, fallback);
            return fallback.toString();
        }
    }

    private String formatRows(List<Map<String, Object>> rows) {
        if (rows == null || rows.isEmpty()) {
            return "(결과 없음)";
        }
        return rows.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }
}
