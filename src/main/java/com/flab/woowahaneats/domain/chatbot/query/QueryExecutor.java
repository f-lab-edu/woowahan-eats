package com.flab.woowahaneats.domain.chatbot.query;

import com.flab.woowahaneats.domain.chatbot.retrieval.Intent;

import java.util.List;
import java.util.Map;

public interface QueryExecutor {

    List<Map<String, Object>> execute(Intent intent, String templateName,
                                       Long restaurantId, QueryParameters params);
}
