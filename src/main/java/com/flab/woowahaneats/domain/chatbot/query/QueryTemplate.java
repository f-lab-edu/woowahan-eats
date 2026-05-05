package com.flab.woowahaneats.domain.chatbot.query;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public record QueryTemplate(String name, String sql) {

    public Map<String, Object> buildParams(Long restaurantId, QueryParameters params) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("restaurantId", restaurantId);

        if (params.startDate() != null) {
            paramMap.put("startDate", LocalDate.parse(params.startDate()).atStartOfDay());
        }
        if (params.endDate() != null) {
            paramMap.put("endDate", LocalDate.parse(params.endDate()).atStartOfDay());
        }
        if (params.menuKeyword() != null) {
            paramMap.put("menuKeyword", params.menuKeyword());
        }

        return paramMap;
    }
}
