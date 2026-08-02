# Upadhya

**AI Learning Mentor**

Upadhya is a privacy-first multilingual learning platform intended to give every student a curriculum-grounded mentor regardless of language, location, or financial background.

## Current MVP

The first milestone targets Tamil Nadu State Board, Standard 3, Mathematics, Term 1, English medium. It accepts an approved textbook PDF, records its metadata and page count, and stores the source locally. Page text extraction, RAG, AI answers, authentication, voice, agents, MCP, and the Flutter client are deliberately outside this milestone.

## Architecture and stack

The backend is a Java 21 / Spring Boot 3 modular monolith organized by feature. PostgreSQL owns transactional metadata; Flyway owns the `upadhya` schema; PDFBox validates PDFs and reads page counts. Qdrant is provisioned for a later retrieval milestone but is not connected to the application. The future mobile client will use Flutter.

All local infrastructure is isolated under the Compose project `upadhya`, network `upadhya-network`, dedicated containers, and dedicated named volumes. Upadhya does not use the common host ports 8080, 5432, 6333, or 6334.

## Local setup

Requirements: Java 21, Maven 3.9+, Docker with Compose, and PowerShell (for the supplied port checker).

```powershell
Copy-Item .env.example .env
# Change UPADHYA_POSTGRES_PASSWORD in .env before starting services.
powershell -NoProfile -ExecutionPolicy Bypass -File ./scripts/check-upadhya-ports.ps1
docker compose --env-file .env -f infrastructure/docker-compose.yml config
docker compose --env-file .env -f infrastructure/docker-compose.yml up -d
```

The port checker only reports conflicts. It never terminates a process. If a port is occupied, choose another Upadhya host port in `.env`, rerun the checker, and then start Compose.

To stop Upadhya services without deleting data:

```powershell
docker compose --env-file .env -f infrastructure/docker-compose.yml down
```

## Backend

The host-run profile connects to `localhost:${UPADHYA_POSTGRES_HOST_PORT}`. Load `.env` into your shell (or set the variables in your IDE), then run:

```powershell
cd backend
$env:SPRING_PROFILES_ACTIVE = 'local'
$env:UPADHYA_POSTGRES_PASSWORD = 'your-local-password'
mvn clean test
mvn spring-boot:run
```

Dockerized application deployments must select the `docker` profile, which connects to `upadhya-postgres:5432`.

Health check:

```bash
curl http://localhost:8091/api/v1/health
```

Upload a textbook:

```bash
curl -X POST http://localhost:8091/api/v1/textbooks \
  -F "file=@/path/to/textbook.pdf;type=application/pdf" \
  -F "title=Standard 3 Mathematics Term 1" \
  -F "board=Tamil Nadu State Board" -F "grade=3" \
  -F "subject=Mathematics" -F "term=Term 1" \
  -F "language=English" -F "edition=2025"
```

Uploaded files are stored only in `./runtime/upadhya/textbooks` by default and are ignored by Git.

## Roadmap

1. Secure PDF ingestion and metadata (current milestone)
2. Page-level extraction with traceable citations
3. Curriculum-grounded retrieval and evaluated answers
4. Assessments and local learner memory
5. Multilingual voice, visual learning, agents, and MCP tools
6. Flutter learner application

## Privacy-first principles

Collect the minimum student data, keep textbook provenance visible, separate learner data from model operations, require explicit approval for content, retain locally where practical, and never present unverified generated material as curriculum truth. See [Privacy and Safety](docs/PRIVACY_AND_SAFETY.md).
