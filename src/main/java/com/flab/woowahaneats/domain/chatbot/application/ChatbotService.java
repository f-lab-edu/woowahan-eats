package com.flab.woowahaneats.domain.chatbot.application;

import com.flab.woowahaneats.domain.chatbot.controller.dto.ChatResponse;
import reactor.core.publisher.Flux;

public interface ChatbotService {

    ChatResponse ask(String message);

    Flux<String> askStream(String message);
}