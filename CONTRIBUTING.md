# Contributing

Use Java 21 and constructor injection, keep changes within a feature boundary, and add tests for behavior. Run `mvn clean test` from `backend/` and validate Compose before requesting review. Never commit `.env`, secrets, uploaded PDFs, student data, or local database files. Flyway migrations are append-only after release.
