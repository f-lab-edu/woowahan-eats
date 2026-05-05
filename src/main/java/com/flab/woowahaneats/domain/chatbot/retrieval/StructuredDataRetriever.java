package com.flab.woowahaneats.domain.chatbot.retrieval;

import com.flab.woowahaneats.domain.chatbot.query.ParameterExtractor;
import com.flab.woowahaneats.domain.chatbot.query.QueryExecutor;
import com.flab.woowahaneats.domain.chatbot.query.QueryParameters;
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
    private final QueryExecutor queryExecutor;

    @Override
    public boolean supports(DataQuery query) {
        return query.intent() != Intent.FAQ_POLICY;
    }

    @Override
    public RetrievalResult retrieve(DataQuery query) {
        QueryParameters params = parameterExtractor.extract(query.originalMessage(), query.intent());
        log.info("추출된 파라미터: {}", params);

        String templateName = params.templateName() != null ? params.templateName() : "";

        List<Map<String, Object>> rows = queryExecutor.execute(
                query.intent(), templateName, query.restaurantId(), params);

        String data = formatRows(rows);
        return RetrievalResult.of(data, templateName);
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
