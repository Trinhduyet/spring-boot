# Repository guide

## Project overview

Learning-focused Java 21 / Spring Boot REST API for a senior .NET developer. Runtime development targets the existing SQL Server. The default test profile uses H2 and must not require Docker, external SQL Server, or Testcontainers.

## Architecture and naming

- Use package-by-feature with explicit subpackages when they have real responsibilities: `feature.api`, `feature.application`, `feature.domain`, and `feature.infrastructure.persistence`.
- Put controllers and HTTP request/response records in `api`; use-case services and cross-feature contracts in `application`; framework-free business types in `domain`; JPA entities/repositories/adapters in `infrastructure`.
- Keep the application class at the root package so Spring component/entity/repository scanning includes all features. Add ArchUnit rules when introducing a boundary.
- Keep shared code in `common` only when multiple features truly share behavior.
- Do not create empty packages, extra Maven modules, or interfaces for every class.
- Use lower-case package names and descriptive Java type names.
- Controllers bind/validate HTTP input and select HTTP responses; application services own use-case rules and transaction boundaries; infrastructure repositories own persistence queries.
- Cross-feature calls target narrow application contracts. Do not import another feature's JPA entities or repositories.

## Coding conventions

- Constructor injection only; no field injection.
- Keep DTOs separate from JPA entities. Use records for immutable transport values where suitable.
- API DTOs must not import JPA entities; map entities to response records in the application use case or a feature mapper.
- Use `BigDecimal` for money, `Instant` for UTC timestamps, and explicit null/absence semantics.
- Prefer readable Java and standard Spring conventions over mechanical .NET translations.
- Do not put business logic in controllers or persistence logic in DTO mappers.

## Database and transactions

- Use environment variables for SQL Server connection values; do not commit credentials.
- Never point schema creation/update at an existing database. Inspect current schema and migration history before changing mappings or enabling validation.
- Keep transaction boundaries on public service use cases and understand proxy/self-invocation behavior.
- H2 tests teach repository and transaction behavior but do not prove SQL Server compatibility, locking, or migration behavior.

## Tests and commands

- Unit-test business rules with JUnit 5, Mockito, and AssertJ.
- Use MockMvc for HTTP contracts and `@DataJpaTest` with H2 for repository behavior.
- Keep the default test suite fast, deterministic, and independent of Docker or external services.
- Run `./mvnw clean test` (Windows: `./mvnw.cmd clean test`) after code changes.

## Documentation and learning

- Learning documents explain concept, Java example, .NET equivalent, differences, pitfalls, practice lab, and source links.
- Update README when setup, commands, API behavior, or learning path changes.
- Keep docs tied to current code; label SQL Server and deployment checks that were not performed.

## Definition of Done

- Code follows feature/layer boundaries and compiles on Java 21.
- Relevant unit, MVC, and H2 tests pass without external infrastructure.
- API validation/errors and docs match implementation.
- No secrets are committed and no claim exceeds the verification evidence.

## Avoid

Testcontainers, Jenkins, SonarQube, Keycloak, Kubernetes, Helm, Argo CD, Kafka, microservices, production monitoring stacks, field injection, entity serialization, unbounded list endpoints, destructive schema automation, and unnecessary abstraction.
