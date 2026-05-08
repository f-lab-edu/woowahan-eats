package com.flab.woowahaneats.domain.chatbot.query;

import com.flab.woowahaneats.domain.chatbot.retrieval.RetrievalResult;

public interface DynamicQueryExecutor {

    RetrievalResult execute(String question, Long restaurantId);
}
