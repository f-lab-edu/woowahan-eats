package com.flab.woowahaneats.domain.chatbot.ingestion;

import org.springframework.ai.document.Document;

import java.util.List;

record PreparedDocument(
        IngestTarget target,
        List<Document> chunks
) {
}
