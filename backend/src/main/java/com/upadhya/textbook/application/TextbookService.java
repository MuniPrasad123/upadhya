package com.upadhya.textbook.application;

import com.upadhya.common.exception.*;
import com.upadhya.textbook.api.*;
import com.upadhya.textbook.domain.*;
import com.upadhya.textbook.infrastructure.TextbookRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Service
public class TextbookService {
    private static final Logger log = LoggerFactory.getLogger(TextbookService.class);
    private final TextbookRepository repository;
    private final Path uploadDirectory;
    private final long maxPdfSize;

    public TextbookService(TextbookRepository repository,
                           @Value("${upadhya.textbook.upload-dir}") String uploadDirectory,
                           @Value("${upadhya.textbook.max-pdf-size-bytes}") long maxPdfSize) {
        this.repository = repository;
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
        this.maxPdfSize = maxPdfSize;
    }

    @Transactional
    public TextbookResponse upload(TextbookUploadRequest request) {
        MultipartFile file = request.file();
        validate(file);
        UUID id = UUID.randomUUID();
        Path target = uploadDirectory.resolve(id + ".pdf").normalize();
        if (!target.startsWith(uploadDirectory)) throw new FileStorageException("Invalid storage path", null);
        try {
            Files.createDirectories(uploadDirectory);
            int pages;
            try (InputStream input = file.getInputStream(); PDDocument document = Loader.loadPDF(input.readAllBytes())) {
                pages = document.getNumberOfPages();
            }
            try (InputStream input = file.getInputStream()) {
                Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
            }
            Instant now = Instant.now();
            Textbook textbook = new Textbook(id, request.title(), request.board(), request.grade(), request.subject(),
                    request.term(), request.language(), request.edition(), safeFileName(file.getOriginalFilename()),
                    pages, TextbookStatus.UPLOADED, now, now);
            Textbook saved = repository.save(textbook);
            log.info("event=textbook_uploaded textbookId={} pages={} fileName={}", id, pages, saved.getOriginalFileName());
            return TextbookResponse.from(saved);
        } catch (IOException | RuntimeException ex) {
            try { Files.deleteIfExists(target); } catch (IOException cleanup) { ex.addSuppressed(cleanup); }
            throw new FileStorageException("Failed to process PDF", ex);
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new UnsupportedFileException("A non-empty PDF file is required");
        if (file.getSize() > maxPdfSize) throw new FileTooLargeException("PDF exceeds the configured maximum size");
        String name = safeFileName(file.getOriginalFilename()).toLowerCase(Locale.ROOT);
        String type = file.getContentType();
        if (!name.endsWith(".pdf") || (type != null && !"application/pdf".equalsIgnoreCase(type))) {
            throw new UnsupportedFileException("Only PDF files are supported");
        }
    }

    private String safeFileName(String original) {
        if (original == null || original.isBlank()) return "textbook.pdf";
        return Path.of(original).getFileName().toString().replaceAll("[\\r\\n]", "_");
    }
}
