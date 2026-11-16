# Clean architecture in this repository

The repository uses package-by-feature with explicit `api`, `application`, `domain`, and `infrastructure` groups when they have real classes. It remains one Spring Boot application rather than a multi-module template.

```text
feature.api -> feature.application -> feature.domain
                       ^
                       | application contracts
feature.infrastructure -- implements those contracts
```

API controllers call application services and do not depend on persistence. Domain types have no framework or outer-layer dependencies. Product lookup, Inventory reservation/release, and Audit recording are explicit application contracts used across feature boundaries. ArchUnit checks API/infrastructure separation, domain independence, and package cycles.

This implementation keeps same-feature Spring Data interfaces and JPA entity types in application services to avoid introducing duplicate domain/persistence models without a demonstrated need. That is a pragmatic feature-based Clean Architecture, not a persistence-agnostic domain model. If persistence replacement or domain isolation becomes a requirement, add a feature-owned persistence port, domain model, and infrastructure mapper together.

The ProductCategory association remains Category-owned. Product's category-filter repository query refers to the mapped table by its stable SQL table/column names to avoid importing Category's persistence entity. H2 covers the query behavior; SQL Server execution remains unverified.

Do not return JPA entities across feature or HTTP boundaries. Keep DTOs independent of JPA. Spring transactions and security use proxies; self-invocation bypasses proxy advice. See [ADR 0001](../adr/0001-architecture.md) and [ADR 0004](../adr/0004-feature-clean-architecture-packages.md).

Responsibility subpackages and the current request flow are documented in [project structure](project-structure.md) and [ADR 0005](../adr/0005-responsibility-packages.md). Application code must not depend on HTTP DTOs; input/result/port values remain framework-free.
