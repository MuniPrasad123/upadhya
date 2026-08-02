package com.upadhya.textbook.infrastructure;

import com.upadhya.textbook.domain.Textbook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TextbookRepository extends JpaRepository<Textbook, UUID> {
}
