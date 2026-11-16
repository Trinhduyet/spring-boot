# JPA mapping

JPA entities map to explicit table and column names. IDs are `IDENTITY`, versioned catalog/order rows use `@Version`, and the persistence context is not exposed to web serialization. `spring.jpa.open-in-view=false`; service methods define transaction boundaries. Hibernate is configured with `ddl-auto=validate` so schema drift fails startup without changing the database.

Use `BigDecimal` for currency and UTC `Instant` for event timestamps. Keep SQL Server-specific DDL in versioned Flyway migrations. The inventory update is the only current native statement; its timestamp uses standard `CURRENT_TIMESTAMP`. Prefer JPQL and repository methods for new persistence behavior.


