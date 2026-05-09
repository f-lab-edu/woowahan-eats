package com.flab.woowahaneats.domain.chatbot.query;

import com.flab.woowahaneats.domain.chatbot.intent.Intent;

public interface QueryParameterValidator {

    QueryParameters validateAndFix(QueryParameters params, Intent intent);
}
