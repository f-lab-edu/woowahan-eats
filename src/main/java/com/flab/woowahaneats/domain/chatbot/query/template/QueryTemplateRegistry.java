package com.flab.woowahaneats.domain.chatbot.query.template;

import com.flab.woowahaneats.domain.chatbot.intent.Intent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class QueryTemplateRegistry {

    private static final String TEMPLATES_PATH = "prompts/chatbot/query-templates.yml";

    private final Map<Intent, List<QueryTemplate>> templates;

    public QueryTemplateRegistry() {
        this.templates = loadTemplates();
    }

    public boolean hasTemplate(Intent intent, String templateName) {
        List<QueryTemplate> intentTemplates = templates.get(intent);
        if (intentTemplates == null) {
            return false;
        }
        return intentTemplates.stream().anyMatch(t -> t.name().equals(templateName));
    }

    public QueryTemplate findTemplate(Intent intent, String templateName) {
        List<QueryTemplate> intentTemplates = templates.get(intent);
        if (intentTemplates == null) {
            throw new IllegalArgumentException("지원하지 않는 Intent입니다: " + intent);
        }

        return intentTemplates.stream()
                .filter(t -> t.name().equals(templateName))
                .findFirst()
                .orElse(intentTemplates.get(0));
    }

    @SuppressWarnings("unchecked")
    private Map<Intent, List<QueryTemplate>> loadTemplates() {
        try (InputStream is = new ClassPathResource(TEMPLATES_PATH).getInputStream()) {
            Yaml yaml = new Yaml();
            Map<String, List<Map<String, String>>> raw = yaml.load(is);

            Map<Intent, List<QueryTemplate>> result = new EnumMap<>(Intent.class);
            for (Map.Entry<String, List<Map<String, String>>> entry : raw.entrySet()) {
                Intent intent = Intent.valueOf(entry.getKey());
                List<QueryTemplate> list = entry.getValue().stream()
                        .map(m -> new QueryTemplate(m.get("name"), m.get("sql").trim()))
                        .toList();
                result.put(intent, list);
            }
            return result;
        } catch (IOException e) {
            throw new IllegalStateException("쿼리 템플릿 파일을 로드할 수 없습니다: " + TEMPLATES_PATH, e);
        }
    }
}
