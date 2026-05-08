package com.flab.woowahaneats.domain.chatbot.ingestion;

import org.springframework.core.io.Resource;

record IngestTarget(
        Resource resource,
        String filePath,
        String hash,
        DocumentIndexMetadata existingMetadata
) {
}
