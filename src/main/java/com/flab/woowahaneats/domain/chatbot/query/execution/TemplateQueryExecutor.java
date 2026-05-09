package com.flab.woowahaneats.domain.chatbot.query.execution;

import com.flab.woowahaneats.domain.chatbot.intent.Intent;
import com.flab.woowahaneats.domain.chatbot.query.QueryExecutor;
import com.flab.woowahaneats.domain.chatbot.query.QueryParameters;
import com.flab.woowahaneats.domain.chatbot.query.template.QueryTemplate;
import com.flab.woowahaneats.domain.chatbot.query.template.QueryTemplateRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TemplateQueryExecutor implements QueryExecutor {

    private final QueryTemplateRegistry templateRegistry;
    private final SqlExecutor sqlExecutor;

    @Override
    public List<Map<String, Object>> execute(Intent intent, String templateName,
                                              Long restaurantId, QueryParameters params) {
        QueryTemplate template = templateRegistry.findTemplate(intent, templateName);
        log.info("선택된 템플릿: {}", template.name());

        Map<String, Object> paramMap = template.buildParams(restaurantId, params);
        log.info("바인딩 파라미터: {}", paramMap);

        return sqlExecutor.execute(template.sql(), paramMap);
    }
}
