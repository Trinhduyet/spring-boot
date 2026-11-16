# Application schema

The reviewed application-owned schema is represented by Flyway V1/V2 under `src/main/resources/db/migration`. Tables are `products`, `categories`, `product_categories`, `inventory`, `inventory_movements`, `customer_orders`, `order_lines`, and `audit_records`. IDs use SQL Server `BIGINT IDENTITY`; timestamps are UTC `DATETIME2`; money is `DECIMAL(14,2)`; booleans use `BIT`. Constraints protect non-negative values, valid order states, idempotency keys, and module relationships.

This is a target schema, not a claim about the user's pre-existing SQL Server. Compare it with the actual catalog and migration history before applying to a populated database. Changes must be additive/backward-compatible where possible and delivered through a separately reviewed Flyway version.

The optional PostgreSQL profile uses a separate target schema in `src/main/resources/db/migration-postgresql`: identity columns, `BOOLEAN`, `NUMERIC(14,2)`, UTC `TIMESTAMP(6) WITHOUT TIME ZONE`, and case-insensitive unique indexes. Inspect the PostgreSQL database independently before applying those scripts; SQL Server Flyway history cannot be reused as PostgreSQL history. See [PostgreSQL and Railway](postgresql-railway.md).


