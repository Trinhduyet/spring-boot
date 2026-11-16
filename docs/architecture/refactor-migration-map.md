# Clean architecture refactor migration map

## Audit before refactoring

The source contains 58 production classes and 11 test classes. `CatalogApplication` is in `com.example.catalog`, so default component scanning currently covers all child packages. There are no explicit `@EntityScan`, `@EnableJpaRepositories`, or custom `@ComponentScan` declarations; entity and repository discovery rely on Boot defaults.

| Current package | Current responsibilities | Confirmed boundary issues |
|---|---|---|
| `product` | Controller, HTTP DTOs, application service, Spring Data repository, JPA entity, catalog port/adapter, exceptions and snapshot | Mixed API/application/domain/persistence; `ProductCatalogService` is a service implementation over Spring Data. |
| `category` | Controller, HTTP DTOs, services, Spring Data repositories, JPA category and product-category entities, exceptions | Mixed layers; association ownership is in Category and remains there. |
| `inventory` | Controller, HTTP DTOs, service, repositories, JPA entities, exceptions | Mixed layers; service performs persistence-specific locking/native updates. |
| `ordering` | Controller, HTTP DTOs, service, repositories, JPA entities, status and exceptions | Mixed layers; service orchestrates Product, Inventory and Audit application contracts. |
| `audit` | Service, repository and JPA entity | Persistence implementation is mixed with application entry point. |
| `security` | Spring Security configuration and servlet filter | HTTP/security adapter is appropriately cross-cutting but not explicitly marked infrastructure. |
| `api` | Global Problem Details exception mapping | HTTP-specific shared adapter is appropriately separate from business features. |

The current cross-feature source dependencies are Category → Product catalog lookup; Inventory → Product lookup and Audit recording; Ordering → Product lookup, Inventory reservation/release and Audit recording; shared API error mapping → feature exceptions. No reverse dependency from Product to Category/Inventory/Ordering or from Audit to a business feature was found. Test code additionally imports feature internals to assemble integration fixtures.

Existing tests include MVC/security slice tests, service unit tests, H2 repository tests, an H2 transaction integration test, global error handling, and ArchUnit boundary tests. The only production Spring Boot application class is `CatalogApplication`. Existing base-package placement means the planned child-package moves preserve Boot component, JPA entity and repository scanning without custom scan annotations.

## Planned target structure

Each business feature keeps only the groups it uses:

```text
com.example.catalog
├── CatalogApplication.java
├── api/                                      # shared HTTP error contract
├── product/{api,application,domain,infrastructure/persistence}
├── category/{api,application,domain,infrastructure/persistence}
├── inventory/{api,application,domain,infrastructure/persistence}
├── ordering/{api,application,domain,infrastructure/persistence}
├── audit/{application,infrastructure/persistence}
└── security/infrastructure
```

| Current class responsibility | Destination |
|---|---|
| Controllers, request/response DTOs | `<feature>.api` |
| Use-case services and cross-feature application contracts | `<feature>.application` |
| Business exceptions and order status | `<feature>.domain` |
| JPA entities, Spring Data repositories and persistence adapters | `<feature>.infrastructure.persistence` |
| Security filter/configuration | `security.infrastructure` |
| Global exception handler | `api` (shared HTTP adapter) |
| Tests | Mirror production packages under `src/test/java` |

`ProductCategoryEntity`, `ProductCategoryId`, and `ProductCategoryRepository` stay owned by Category because Category currently owns assignment/deletion semantics for the association. `ProductSnapshot` remains an application-facing read model; `OrderLineEntity` remains owned by Ordering and stores a product snapshot rather than a live Product association. These moves change Java package names only; explicit JPA table/column mappings and migrations must remain unchanged.

## Migration plan and verification risks

1. Record this map before moves.
2. Move each class to the layer that owns its current responsibility; update package declarations and imports, preserving class and entity names.
3. Add narrowly scoped adapters/ports only where a real cross-feature dependency exists; do not add interfaces for every use case.
4. Add ArchUnit rules for API/persistence separation, domain independence, dependency cycles and cross-feature infrastructure access.
5. Update architecture docs, README, AGENTS and local architecture/API skills.
6. Run `./mvnw.cmd -B -ntp clean verify`; use H2 tests to verify mappings and transactions. This is not SQL Server runtime or migration proof.

Main risks are missed same-feature imports after package splitting, Spring slice-test configuration, and accidental table/column changes from entity edits. The refactor must keep entity class names and annotations, leave SQL migrations untouched, and verify all package references before reporting completion.



## Completed migration inventory

The production tree now has 60 Java classes (58 pre-refactor classes plus `AuditRecorder`, `InventoryOperations`, and the extracted `JpaAuditRecorder` adapter). The 11 test classes were moved to mirror their owning production layer. `CatalogApplication` and the shared `ApiExceptionHandler` remain in their existing root/shared packages.

| Feature | API | Application | Domain | Infrastructure |
|---|---|---|---|---|
| Product | `ProductController`, `CreateProductRequest`, `UpdateProductRequest`, `ProductResponse` | `ProductService`, `ProductCatalog`, `ProductSnapshot` | `ProductNotFoundException`, `SkuConflictException` | `ProductEntity`, `ProductRepository`, `ProductCatalogAdapter` |
| Category | `CategoryController`, `CategoryRequest`, `CategoryResponse` | `CategoryService` | `CategoryNotFoundException`, `CategoryConflictException`, `CategoryInUseException` | `CategoryEntity`, `CategoryRepository`, `ProductCategoryEntity`, `ProductCategoryId`, `ProductCategoryRepository` |
| Inventory | `InventoryController`, `InventoryRequest`, `InventoryAdjustmentRequest`, `InventoryResponse`, `InventoryMovementResponse` | `InventoryService`, `InventoryOperations` | `InventoryNotFoundException`, `InsufficientStockException` | `InventoryEntity`, `InventoryMovementEntity`, `InventoryRepository`, `InventoryMovementRepository` |
| Ordering | `OrderController`, `CreateOrderRequest`, `OrderLineRequest`, `OrderLineResponse`, `OrderResponse` | `OrderService` | `OrderStatus`, `OrderNotFoundException`, `OrderProductNotFoundException`, `InvalidOrderStateException`, `IdempotencyConflictException` | `OrderEntity`, `OrderLineEntity`, `OrderRepository`, `OrderLineRepository` |
| Audit | — | `AuditRecorder` | — | `AuditRecordEntity`, `AuditRecordRepository`, `JpaAuditRecorder` |
| Security | — | — | — | `SecurityConfig`, `CorrelationIdFilter` under `security.infrastructure` |

Tests mirror these packages: Product API/application/persistence, Category application/persistence, Inventory application, Ordering application, Security infrastructure, shared API, and architecture. The migration map was written before package moves; this inventory records the completed destination map.

## Completed dependency work

- Product exposes `ProductCatalog`; its JPA-backed implementation is `ProductCatalogAdapter` in Product infrastructure.
- Ordering calls Inventory through `InventoryOperations` rather than depending on `InventoryService` directly.
- Inventory and Ordering record audit data through the `AuditRecorder` application port; Audit infrastructure supplies `JpaAuditRecorder`.
- Response DTOs no longer import JPA entities; response mapping sits in application services.
- Product's category query uses the existing `product_categories` table/columns and no longer imports Category's association entity.
- ArchUnit enforces API-to-infrastructure separation, domain independence, no business-feature package cycles, and no cross-feature infrastructure imports.
- No route/controller mappings or migration files were changed. JPA entity classes and explicit table/column annotations were preserved.

## Verification result

- `./mvnw.cmd -B -ntp clean verify`: PASS on 2026-11-11. 25 tests passed; Checkstyle reported 0 violations; JaCoCo coverage gate passed.
- The test profile uses H2. Repository/context/transaction tests confirm Boot scans the moved entities and repositories; the added category-filter test confirms the table query and page count behavior on H2.
- Product MVC and security MockMvc tests passed. Other controllers were structurally moved without changing their route annotations; no live HTTP server or SQL Server was started for this refactor.
- 76 Markdown files were scanned for relative links; no broken relative links were found.
- No Flyway migration files or JPA table/column annotations were changed. SQL Server native-query behavior, migrations, and runtime schema validation remain unverified.

## Subsequent responsibility split

The initial layer migration above is historical. ADR 0005 further separates controllers, request/response DTOs, inputs/results, services, ports, entities, repositories and adapters. Use [project structure](project-structure.md) for current paths.
