package com.flab.woowahaneats.domain.chatbot.application;

import com.flab.woowahaneats.domain.chatbot.exception.LlmCallException;
import com.flab.woowahaneats.domain.chatbot.prompt.PromptProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerSynthesisService {

    private static final String PROMPT_ID = "answer-synthesis";

    private final ChatClient chatClient;
    private final PromptProvider promptProvider;

    public String synthesize(String question, String resultText) {
        try {
            String systemPrompt = promptProvider.getSystemPrompt(PROMPT_ID, Map.of());
            String userPrompt = promptProvider.getUserPrompt(PROMPT_ID, Map.of(
                    "question", question,
                    "result", resultText
            ));

            return chatClient
                    .prompt()
                    .system(systemPrompt)
                    .user(userPrompt)
                    .options(promptProvider.getOptions(PROMPT_ID))
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("답변 합성 중 LLM 호출 실패: {}", e.getMessage(), e);
            throw new LlmCallException("답변 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    public Flux<String> synthesizeStream(String question, String resultText) {
        String systemPrompt = promptProvider.getSystemPrompt(PROMPT_ID, Map.of());
        String userPrompt = promptProvider.getUserPrompt(PROMPT_ID, Map.of(
                "question", question,
                "result", resultText
        ));

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .options(promptProvider.getOptions(PROMPT_ID))
                .stream()
                .content()
                .onErrorResume(e -> {
                    log.error("스트리밍 답변 합성 중 LLM 호출 실패: {}", e.getMessage(), e);
                    return Flux.error(new LlmCallException(
                            "답변 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
                });
    }
}
