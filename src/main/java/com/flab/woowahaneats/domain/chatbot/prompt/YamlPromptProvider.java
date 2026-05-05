package com.flab.woowahaneats.domain.chatbot.prompt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class YamlPromptProvider implements PromptProvider {

    private static final String PROMPTS_DIR = "prompts/chatbot/";
    private final Map<String, Map<String, Object>> cache = new ConcurrentHashMap<>();

    @Override
    public String getSystemPrompt(String promptId, Map<String, Object> variables) {
        String template = getField(promptId, "system");
        return resolveVariables(template, variables);
    }

    @Override
    public String getUserPrompt(String promptId, Map<String, Object> variables) {
        String template = getField(promptId, "user");
        return resolveVariables(template, variables);
    }

    @Override
    public ChatOptions getOptions(String promptId) {
        Map<String, Object> promptData = loadPrompt(promptId);
        @SuppressWarnings("unchecked")
        Map<String, Object> options = (Map<String, Object>) promptData.get("options");
        if (options == null) {
            return ChatOptions.builder().build();
        }

        ChatOptions.Builder builder = ChatOptions.builder();
        if (options.containsKey("temperature")) {
            builder.temperature(((Number) options.get("temperature")).doubleValue());
        }
        if (options.containsKey("maxTokens")) {
            builder.maxTokens(((Number) options.get("maxTokens")).intValue());
        }
        return builder.build();
    }

    private String getField(String promptId, String field) {
        Map<String, Object> promptData = loadPrompt(promptId);
        Object value = promptData.get(field);
        if (value == null) {
            throw new IllegalStateException(
                    "프롬프트 '" + promptId + "'에 '" + field + "' 필드가 없습니다.");
        }
        return value.toString();
    }

    private Map<String, Object> loadPrompt(String promptId) {
        return cache.computeIfAbsent(promptId, this::loadFromFile);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadFromFile(String promptId) {
        String path = PROMPTS_DIR + promptId + ".yml";
        try {
            ClassPathResource resource = new ClassPathResource(path);
            try (InputStream is = resource.getInputStream()) {
                Yaml yaml = new Yaml();
                return yaml.load(is);
            }
        } catch (IOException e) {
            throw new IllegalStateException("프롬프트 파일을 로드할 수 없습니다: " + path, e);
        }
    }

    private String resolveVariables(String template, Map<String, Object> variables) {
        if (variables == null || variables.isEmpty()) {
            return template;
        }
        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return result;
    }
}
