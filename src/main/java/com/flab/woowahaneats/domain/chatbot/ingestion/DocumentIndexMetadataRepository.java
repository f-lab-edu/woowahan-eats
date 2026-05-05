package com.flab.woowahaneats.domain.chatbot.ingestion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentIndexMetadataRepository extends JpaRepository<DocumentIndexMetadata, Long> {

    Optional<DocumentIndexMetadata> findByFilePath(String filePath);

    List<DocumentIndexMetadata> findAllByCategory(String category);
}
