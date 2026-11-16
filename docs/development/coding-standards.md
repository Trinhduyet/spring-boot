# Coding standards

- Keep source package-by-feature: `feature.api`, `feature.application`, `feature.domain`, and `feature.infrastructure` when those groups have actual responsibilities.
- Keep controllers transport-only. Use Jakarta Validation on API request records and response records rather than returning JPA entities.
- Use constructor injection. Do not introduce service interfaces without a real substitution or cross-feature boundary.
- Place Spring Data repositories, JPA entities, locking queries, and persistence adapters under `feature.infrastructure.persistence`.
- Keep domain types free of Spring, HTTP, and persistence imports.
- Put transaction boundaries on public application use cases. Account for proxy/self-invocation behavior.
- Prefer `BigDecimal` for money, `Instant` for UTC timestamps, explicit null semantics, and readable Java 21.
- Keep shared packages technical and genuinely shared; do not use them as feature dumping grounds.
- Add/update ArchUnit boundary rules and tests with package moves.
- Never enable schema create/update against the existing SQL Server. Preserve explicit table/column mappings and review migrations before changing them.

H2 tests validate fast repository and transaction behavior only; they do not prove SQL Server locking, dialect, collation, or migration compatibility.


