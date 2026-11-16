# ADR 0006: PostgreSQL profile and Swagger access

Status: Accepted

## Context

The Railway deployment needs a PostgreSQL service in its private project network. The existing SQL Server migrations use vendor-specific syntax and have already been applied to a separate SQL Server database. Swagger was denied by the protected security chain.

## Decision

Add a `postgres` profile, PostgreSQL JDBC/Flyway dependencies, a UTC session setting and a separate migration location. Keep SQL Server configuration and migration history intact. Flyway remains disabled by default and is enabled only after inspecting and reviewing the actual target database. Hibernate validates rather than modifies the runtime schema.

Permit Swagger UI and OpenAPI paths in the protected chain, retain HTTP Basic for `/api/**`, and advertise Basic authentication in OpenAPI for the protected profile. Keep the `local`/`test` permit-all profiles limited to isolated development/tests.

## Consequences

PostgreSQL and SQL Server have separate schemas and are not data-synchronized. PostgreSQL case-insensitive unique indexes and UTC timestamp conventions must be reviewed along with the application queries. Public API documentation exposes endpoint metadata. HTTP Basic is a learning configuration and requires TLS; it is not a production identity system. H2 tests do not prove PostgreSQL locking or migrations.
