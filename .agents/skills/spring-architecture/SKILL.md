---
name: spring-architecture
description: Plan or review package boundaries, Spring module structure, DDD use cases, dependency direction, architecture tests, or ADRs for this modular monolith.
---

# Architecture workflow

1. Map business ownership and production dependencies before editing. Record current packages, cross-feature calls, and Spring scanning assumptions.
2. Keep package-by-feature, with `api`, `application`, `domain`, and `infrastructure` subpackages only where they add clarity.
3. Keep controllers/transport DTOs in API, use-case coordination and real application contracts in Application, framework-free rules in Domain, and Spring/JPA adapters in Infrastructure.
4. Do not create an interface for every service. Add a port when another feature or technology boundary benefits from it; do not pass entities or repositories across feature boundaries.
5. Preserve entity class mappings, table/column names, routes and behavior during package-only refactors. Inspect every consumer and test slice.
6. Add ArchUnit checks for API/infrastructure separation, domain independence, dependency cycles, and cross-feature boundaries. Update Mermaid diagrams and ownership docs.
7. Record material topology changes in an ADR. Verify component, entity and repository scanning from the root application package.

## Checklist

- [ ] No global service/repository package growth or empty layer packages.
- [ ] Shared code has multiple real consumers.
- [ ] No feature imports another feature's persistence internals.
- [ ] Architecture tests match the intended rules and exclude test fixtures where appropriate.
- [ ] H2 proves only its mappings/behavior; SQL Server-specific behavior is clearly labeled.

References: `docs/architecture/`, `docs/adr/`, `AGENTS.md`.
