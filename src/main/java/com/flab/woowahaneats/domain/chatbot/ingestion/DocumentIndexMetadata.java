package com.flab.woowahaneats.domain.chatbot.ingestion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_index_metadata")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentIndexMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String filePath;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, length = 64)
    private String contentHash;

    @Column(nullable = false)
    private LocalDateTime indexedAt;

    @Builder
    private DocumentIndexMetadata(String filePath, String category, String contentHash, LocalDateTime indexedAt) {
        this.filePath = filePath;
        this.category = category;
        this.contentHash = contentHash;
        this.indexedAt = indexedAt;
    }

    public void updateHash(String contentHash) {
        this.contentHash = contentHash;
        this.indexedAt = LocalDateTime.now();
    }
}
