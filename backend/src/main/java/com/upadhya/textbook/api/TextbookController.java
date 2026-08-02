package com.upadhya.textbook.api;

import com.upadhya.textbook.application.TextbookService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/textbooks")
public class TextbookController {
    private final TextbookService service;
    public TextbookController(TextbookService service) { this.service = service; }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TextbookResponse> upload(@Valid @ModelAttribute TextbookUploadRequest request) {
        TextbookResponse response = service.upload(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
