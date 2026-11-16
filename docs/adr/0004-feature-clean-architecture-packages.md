# ADR 0004: Feature-based Clean Architecture packages

- Status: Accepted
- Date: 2026-11-11

## Context

The source used flat package-by-feature folders, so HTTP, application, domain, persistence, and security framework types were mixed. Cross-feature orchestration used application services for product lookup, inventory operations, and audit recording. The application class is rooted at `com.example.catalog`; Spring Boot defaults scan its child packages.

## Decision

Keep one Maven application and organize each business capability by feature, adding `api`, `application`, `domain`, and `infrastructure.persistence` groups where each group has real classes. Keep global Problem Details handling under shared `api` and Spring Security adapters under `security.infrastructure`.

Preserve ProductCategory association ownership in Category. Keep `OrderLineEntity` under Ordering as an order-owned historical snapshot. Product filtering references the association table with a native query to avoid a dependency on Category's JPA entity.

Expose narrow cross-feature application contracts for product lookup, inventory reserve/release, and audit recording. Keep JPA tables, columns, mappings, migrations, routes, and JSON behavior unchanged. Use ArchUnit to enforce API/infrastructure separation, domain independence, and absence of feature package cycles.

This is a pragmatic clean architecture for the current learning scope, not a persistence-agnostic domain model. Same-feature application services still use their Spring Data repository interfaces and JPA entity classes. Introduce separate domain models and persistence ports when that isolation has a concrete benefit.

## Consequences

- Feature code is easier to locate by responsibility while remaining a single Spring Boot component-scan tree.
- HTTP DTOs no longer map directly from entity types; application services perform response mapping.
- Cross-feature implementation details are hidden behind application contracts.
- The Product category-filter query is SQL/table-name coupled; H2 coverage does not prove SQL Server execution. The existing SQL Server schema and migration files are unchanged.
- Full persistence independence is deferred and explicitly documented rather than implied.


