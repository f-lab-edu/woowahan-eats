package com.flab.woowahaneats.domain.chatbot.prompt;

import org.springframework.ai.chat.prompt.ChatOptions;

import java.util.Map;

public interface PromptProvider {

    String getSystemPrompt(String promptId, Map<String, Object> variables);

    String getUserPrompt(String promptId, Map<String, Object> variables);

    ChatOptions getOptions(String promptId);
}
