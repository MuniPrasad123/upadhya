# MVP Scope

The initial curriculum slice is Tamil Nadu State Board, Standard 3, Mathematics, Term 1, English medium.

The first milestone provides a versioned REST API to upload one approved PDF, validate format and size, count its pages, store the source in an Upadhya-specific local directory, and persist metadata in PostgreSQL. It does not extract page text.

AI generation, RAG, Qdrant integration, accounts, authentication, learner profiles, assessment generation, voice, translation, agents, MCP tools, and Flutter are excluded until later milestones. The running Qdrant container is capacity reserved for future work, not an active dependency.
