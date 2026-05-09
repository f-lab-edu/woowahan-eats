package com.flab.woowahaneats.domain.chatbot.query;

import com.flab.woowahaneats.domain.chatbot.intent.Intent;

public interface ParameterExtractor {

    QueryParameters extract(String message, Intent intent);
}
