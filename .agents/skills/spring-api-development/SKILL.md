---
name: spring-api-development
description: Design or change Spring MVC REST endpoints, DTOs, validation, errors, pagination, and OpenAPI contracts in this repository.
---

# API change workflow

1. Locate the owning feature and read its `api`, `application`, and shared error mappings.
2. Define route, method, authentication, request/response, and status semantics before editing.
3. Put controllers and Jakarta-validated request/response records in `feature.api`; keep them independent of JPA entities and repositories.
4. Keep controllers transport-only. Invoke an application use case through constructor injection.
5. Map persistence/domain values to response records in application code or a feature mapper; never serialize an entity.
6. Map expected errors to stable status and useful Problem Details without leaking internals.
7. Update OpenAPI/API docs and controller tests for success, invalid input, not-found/conflict, and security.
8. Run focused tests, then `./mvnw -B -ntp clean verify` on changes.

## Checklist

- [ ] API versioning and compatibility reviewed.
- [ ] Pagination and sort are bounded/allowlisted.
- [ ] No secret or token is logged.
- [ ] API has no infrastructure dependency.
- [ ] Contract, tests, and examples are consistent.

References: `docs/api/`, `docs/architecture/dependency-rules.md`, `docs/development/validation.md`, `AGENTS.md`.
