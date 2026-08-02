package com.upadhya.textbook.api;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public record TextbookUploadRequest(
        @NotNull MultipartFile file,
        @NotBlank @Size(max=255) String title,
        @NotBlank @Size(max=120) String board,
        @NotNull @Min(1) @Max(12) Integer grade,
        @NotBlank @Size(max=120) String subject,
        @NotBlank @Size(max=60) String term,
        @NotBlank @Size(max=60) String language,
        @NotBlank @Size(max=40) String edition) {
}
