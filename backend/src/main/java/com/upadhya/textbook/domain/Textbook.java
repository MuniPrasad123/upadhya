package com.upadhya.textbook.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "textbooks", schema = "upadhya")
public class Textbook {
    @Id private UUID id;
    @Column(nullable=false) private String title;
    @Column(nullable=false) private String board;
    @Column(nullable=false) private Integer grade;
    @Column(nullable=false) private String subject;
    @Column(nullable=false) private String term;
    @Column(nullable=false) private String language;
    @Column(nullable=false) private String edition;
    @Column(name="original_file_name", nullable=false) private String originalFileName;
    @Column(name="total_pages", nullable=false) private Integer totalPages;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private TextbookStatus status;
    @Column(name="created_at", nullable=false, updatable=false) private Instant createdAt;
    @Column(name="updated_at", nullable=false) private Instant updatedAt;

    protected Textbook() {}

    public Textbook(UUID id, String title, String board, Integer grade, String subject, String term,
                    String language, String edition, String originalFileName, Integer totalPages,
                    TextbookStatus status, Instant createdAt, Instant updatedAt) {
        this.id=id; this.title=title; this.board=board; this.grade=grade; this.subject=subject;
        this.term=term; this.language=language; this.edition=edition; this.originalFileName=originalFileName;
        this.totalPages=totalPages; this.status=status; this.createdAt=createdAt; this.updatedAt=updatedAt;
    }
    public UUID getId(){return id;} public String getTitle(){return title;} public String getBoard(){return board;}
    public Integer getGrade(){return grade;} public String getSubject(){return subject;} public String getTerm(){return term;}
    public String getLanguage(){return language;} public String getEdition(){return edition;}
    public String getOriginalFileName(){return originalFileName;} public Integer getTotalPages(){return totalPages;}
    public TextbookStatus getStatus(){return status;} public Instant getCreatedAt(){return createdAt;}
    public Instant getUpdatedAt(){return updatedAt;}
}
