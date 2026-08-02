# Architecture

Upadhya begins as a modular monolith. Feature packages (`textbook`, `tutor`, `assessment`, `learner`, and `safety`) contain API, application, domain, and infrastructure boundaries. Only the textbook feature has behavior in this milestone; the remaining directories reserve clear ownership without pretending to implement features.

Controllers bind and validate DTOs. Application services coordinate file and database work. JPA entities remain internal and response DTOs form the public contract. PostgreSQL stores metadata in the application-specific `upadhya` schema, managed exclusively by Flyway. PDF files use UUID-based storage names so user-supplied names cannot determine filesystem paths.

The `local` profile reaches PostgreSQL through the configurable host port. The `docker` profile uses `upadhya-postgres:5432`. The `test` profile is supplied dynamically by PostgreSQL Testcontainers. Hibernate validates rather than creates the schema.

Local infrastructure uses a dedicated bridge network and named volumes. No network or volume is external, and every Compose resource carries an Upadhya label.
