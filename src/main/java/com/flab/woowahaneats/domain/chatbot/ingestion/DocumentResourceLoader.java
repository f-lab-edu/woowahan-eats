package com.flab.woowahaneats.domain.chatbot.ingestion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

@Slf4j
@Component
public class DocumentResourceLoader {

    public List<Resource> loadResources(String folder) {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:documents/" + folder + "/*.md");
            return Arrays.asList(resources);
        } catch (IOException e) {
            log.error("문서 로드 실패: documents/{}/", folder, e);
            return List.of();
        }
    }

    public String computeHash(Resource resource) {
        try (InputStream is = resource.getInputStream()) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(is.readAllBytes());
            return HexFormat.of().formatHex(hash);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("해시 계산 실패: " + resource.getFilename(), e);
        }
    }
}
