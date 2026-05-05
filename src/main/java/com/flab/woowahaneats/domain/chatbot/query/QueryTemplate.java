package com.flab.woowahaneats.domain.chatbot.query;

public record QueryTemplate(String name, String sql) {

    public String bind(Long restaurantId, QueryParameters params) {
        String bound = sql.replace(":restaurantId", restaurantId.toString());

        if (params.startDate() != null) {
            bound = bound.replace(":startDate", "'" + params.startDate() + "'");
        }
        if (params.endDate() != null) {
            bound = bound.replace(":endDate", "'" + params.endDate() + "'");
        }
        if (params.menuKeyword() != null) {
            bound = bound.replace(":menuKeyword", "'" + params.menuKeyword() + "'");
        }

        return bound;
    }
}
