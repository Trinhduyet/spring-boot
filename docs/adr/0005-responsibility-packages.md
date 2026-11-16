# ADR 0005: Separate roles inside feature layers

Status: Accepted

## Context

The earlier feature refactor grouped controllers with transport DTOs, services with contracts, and JPA entities with repositories/adapters. Services imported API request/response types, while InventoryService executed native SQL directly. The folders did not expose class responsibilities clearly.

## Decision

Keep package-by-feature in one Maven module. Use populated responsibility packages: api/controller, request, response, mapper; application/service, input, result, port, mapper; domain/exception and model; infrastructure/persistence/entity, repository and adapter. Separate security configuration from servlet filters.

Controllers map validated HTTP requests to framework-free inputs and application results back to HTTP responses. Application result mappers handle entity-to-result conversion. Move inventory native SQL and persistence-context operations into InventoryStockWriter while retaining the existing service transactions and business checks. Delete the unused InventoryRequest.

Preserve routes, JSON field names, validation annotations, table mappings and migration files. Keep component scanning rooted at CatalogApplication. Add ArchUnit rules to prevent application-to-HTTP dependencies and enforce role locations.

## Consequences

Readers can locate classes by feature and responsibility. HTTP contracts can evolve independently of service signatures, at the cost of explicit input/result mappings. Own-feature JPA dependencies remain a deliberate compromise; this does not introduce complete persistence inversion. No new Maven modules or service interfaces are required.

Tests cover architecture, HTTP contracts, H2 repositories and transaction rollback/cancellation. H2 coverage does not establish SQL Server locking behavior. Live SQL Server verification for this refactor is limited to application startup and read-only HTTP checks when performed.
