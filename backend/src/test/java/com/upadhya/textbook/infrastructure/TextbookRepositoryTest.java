package com.upadhya.textbook.infrastructure;

import com.upadhya.PostgresIntegrationTest;
import com.upadhya.textbook.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class TextbookRepositoryTest extends PostgresIntegrationTest {
    @Autowired TextbookRepository repository;

    @Test
    void persistsTextbookInPostgresql() {
        Instant now = Instant.now();
        UUID id = UUID.randomUUID();
        repository.saveAndFlush(new Textbook(id, "Math", "Tamil Nadu State Board", 3, "Mathematics",
                "Term 1", "English", "2025", "math.pdf", 66, TextbookStatus.UPLOADED, now, now));
        assertThat(repository.findById(id)).isPresent().get()
                .extracting(Textbook::getTotalPages, Textbook::getStatus)
                .containsExactly(66, TextbookStatus.UPLOADED);
    }
}
