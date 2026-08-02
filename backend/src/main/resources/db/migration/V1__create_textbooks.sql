CREATE TABLE textbooks (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    board VARCHAR(120) NOT NULL,
    grade INTEGER NOT NULL CHECK (grade BETWEEN 1 AND 12),
    subject VARCHAR(120) NOT NULL,
    term VARCHAR(60) NOT NULL,
    language VARCHAR(60) NOT NULL,
    edition VARCHAR(40) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    total_pages INTEGER NOT NULL CHECK (total_pages > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('UPLOADED', 'PROCESSING', 'PROCESSED', 'FAILED')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_textbooks_curriculum
    ON textbooks (board, grade, subject, term, language);
