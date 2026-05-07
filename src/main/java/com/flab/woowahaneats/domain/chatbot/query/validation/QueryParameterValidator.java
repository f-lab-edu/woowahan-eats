package com.flab.woowahaneats.domain.chatbot.query.validation;

import com.flab.woowahaneats.domain.chatbot.intent.Intent;
import com.flab.woowahaneats.domain.chatbot.query.QueryParameters;
import com.flab.woowahaneats.domain.chatbot.query.template.QueryTemplateRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueryParameterValidator {

    private final QueryTemplateRegistry templateRegistry;

    public QueryParameters validateAndFix(QueryParameters params, Intent intent) {
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
}
