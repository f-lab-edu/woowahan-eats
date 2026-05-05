package com.flab.woowahaneats.domain.chatbot.retrieval;

import com.flab.woowahaneats.domain.chatbot.domain.ChatQuestion;
import com.flab.woowahaneats.domain.chatbot.application.SqlGenerationService;
import com.flab.woowahaneats.domain.chatbot.query.SqlExecutor;
import com.flab.woowahaneats.domain.chatbot.query.SqlValidator;
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

    private final SqlGenerationService sqlGenerationService;
    private final SqlValidator sqlValidator;
    private final SqlExecutor sqlExecutor;

    @Override
    public boolean supports(DataQuery query) {
        return query.intent() != Intent.FAQ_POLICY;
    }

    @Override
    public RetrievalResult retrieve(DataQuery query) {
        ChatQuestion question = new ChatQuestion(query.restaurantId(), query.originalMessage());

        String sql = sqlGenerationService.generateSql(question);
        log.info("생성된 SQL: {}", sql);

        sql = sqlValidator.validate(sql, query.restaurantId());
        log.info("검증 후 SQL: {}", sql);

        List<Map<String, Object>> rows = sqlExecutor.execute(sql);

        String data = formatRows(rows);
        return RetrievalResult.of(data, sql);
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
