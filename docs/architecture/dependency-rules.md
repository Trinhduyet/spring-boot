# Dependency rules

These rules apply within the single `com.example.catalog` Maven application. They preserve feature cohesion without creating extra Maven modules.

1. A feature API may depend on its application use cases, domain types, transport records, and shared HTTP error handling. It may not depend on infrastructure packages or Spring Data repositories.
2. Application services own transactions and use-case orchestration. Cross-feature dependencies target application contracts, never another feature's entity or repository.
3. Domain packages depend only on domain and Java types. They do not import Spring, HTTP, application, or infrastructure code.
4. Infrastructure may depend on its feature's application ports/domain types and on Spring/JPA. It implements adapters and persistence access.
5. Business features must not form package dependency cycles. Keep the direction of cross-feature calls explicit in the module diagram.
6. Shared `api` is an HTTP adapter. Shared packages must not depend on feature infrastructure.
7. Use ArchUnit in `ModuleBoundaryTest` for API-to-infrastructure separation, domain independence, and feature cycle detection.

## Current contracts

- `category.application` and `inventory.application` consume `product.application.port.ProductCatalog`.
- `ordering.application` consumes Product catalog lookup, `inventory.application.port.InventoryOperations`, and `audit.application.port.AuditRecorder`.
- `InventoryService` implements `InventoryOperations`; `JpaAuditRecorder` implements `AuditRecorder`.

The current application services retain same-feature Spring Data/JPA dependencies as a deliberate low-abstraction learning boundary. The feature packages are separated, but this is not a fully persistence-agnostic/hexagonal implementation. If adding a new port, return domain or application values rather than another feature's JPA entity.



Responsibility subpackages and the current request flow are documented in [project structure](project-structure.md) and [ADR 0005](../adr/0005-responsibility-packages.md). Application code must not depend on HTTP DTOs; input/result/port values remain framework-free.
