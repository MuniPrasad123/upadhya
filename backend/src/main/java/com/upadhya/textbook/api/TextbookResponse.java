package com.upadhya.textbook.api;

import com.upadhya.textbook.domain.Textbook;
import com.upadhya.textbook.domain.TextbookStatus;
import java.util.UUID;

public record TextbookResponse(UUID id, String title, String board, Integer grade, String subject,
                               String term, String language, String edition, Integer totalPages,
                               TextbookStatus status) {
    public static TextbookResponse from(Textbook textbook) {
        return new TextbookResponse(textbook.getId(), textbook.getTitle(), textbook.getBoard(), textbook.getGrade(),
                textbook.getSubject(), textbook.getTerm(), textbook.getLanguage(), textbook.getEdition(),
                textbook.getTotalPages(), textbook.getStatus());
    }
}
