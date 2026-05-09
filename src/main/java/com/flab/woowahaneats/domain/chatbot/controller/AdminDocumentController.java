package com.flab.woowahaneats.domain.chatbot.controller;

import com.flab.woowahaneats.domain.chatbot.ingestion.DocumentIngestionService;
import com.flab.woowahaneats.domain.chatbot.ingestion.DocumentIngestionService.IngestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/documents")
@RequiredArgsConstructor
public class AdminDocumentController {

    private final DocumentIngestionService ingestionService;

    @PostMapping("/ingest")
    public ResponseEntity<IngestResult> ingestAll() {
        IngestResult result = ingestionService.ingestAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/ingest/{folder}")
    public ResponseEntity<IngestResult> ingestCategory(@PathVariable String folder) {
        String category = ingestionService.resolveCategory(folder);
        IngestResult result = ingestionService.ingestCategory(folder, category);
        return ResponseEntity.ok(result);
    }
}
