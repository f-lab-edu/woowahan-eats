package com.flab.woowahaneats.domain.chatbot.retrieval;

public interface DataRetriever {

    boolean supports(DataQuery query);

    RetrievalResult retrieve(DataQuery query);
}
