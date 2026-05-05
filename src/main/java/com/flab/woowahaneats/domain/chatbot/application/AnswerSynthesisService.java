package com.flab.woowahaneats.domain.chatbot.application;

import com.flab.woowahaneats.domain.chatbot.llm.LlmExecutor;
import com.flab.woowahaneats.domain.chatbot.prompt.PromptProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerSynthesisService {

    private static final String PROMPT_ID = "answer-synthesis";

    private final PromptProvider promptProvider;
    private final LlmExecutor llmExecutor;

    public String synthesize(String question, String resultText) {
        String systemPrompt = promptProvider.getSystemPrompt(PROMPT_ID, Map.of());
        String userPrompt = promptProvider.getUserPrompt(PROMPT_ID, Map.of(
                "question", question,
                "result", resultText
        ));

        return llmExecutor.call(systemPrompt, userPrompt, promptProvider.getOptions(PROMPT_ID));
    }

    public Flux<String> synthesizeStream(String question, String resultText) {
        String systemPrompt = promptProvider.getSystemPrompt(PROMPT_ID, Map.of());
        String userPrompt = promptProvider.getUserPrompt(PROMPT_ID, Map.of(
                "question", question,
                "result", resultText
        ));

        return llmExecutor.stream(systemPrompt, userPrompt, promptProvider.getOptions(PROMPT_ID));
    }
}
