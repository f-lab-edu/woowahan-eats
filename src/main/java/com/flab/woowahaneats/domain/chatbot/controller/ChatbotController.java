package com.flab.woowahaneats.domain.chatbot.controller;

import com.flab.woowahaneats.domain.chatbot.application.ChatbotService;
import com.flab.woowahaneats.domain.chatbot.controller.dto.ChatRequest;
import com.flab.woowahaneats.domain.chatbot.controller.dto.ChatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/owner/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/ask")
    public ChatResponse ask(@Valid @RequestBody ChatRequest request) {
        return chatbotService.ask(request.message());
    }

    @PostMapping(value = "/ask/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> askStream(@Valid @RequestBody ChatRequest request) {
        return chatbotService.askStream(request.message());
    }
}