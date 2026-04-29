package com.flab.woowahaneats.domain.chatbot.prompt;

import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Getter
@Component
public class SchemaLoader {

    private static final String SCHEMA_PATH = "prompts/text-to-sql/v1/schema-summary.md";

    private final String schemaSummary;

    public SchemaLoader() {
        this.schemaSummary = loadSchema();
    }

    private String loadSchema() {
        try {
            ClassPathResource resource = new ClassPathResource(SCHEMA_PATH);
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("스키마 요약 파일을 로드할 수 없습니다: " + SCHEMA_PATH, e);
        }
    }
}