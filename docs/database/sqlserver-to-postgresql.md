# Future SQL Server to PostgreSQL path

PostgreSQL is not a current runtime dependency or deployment target. A future move requires separate vendor-specific Flyway scripts and a deliberate Hibernate dialect/configuration profile; the SQL Server migrations must not be assumed portable. Preserve `BigDecimal`, UTC `Instant`, JPQL, repository abstractions and module contracts. Keep vendor-specific locking and DDL isolated.

Migration plan: inventory data and SQL Server-specific features; map types/collations/identity behavior; create and review PostgreSQL DDL; perform an offline test conversion; copy a production-like snapshot; reconcile counts, keys, decimals, timestamps and application-level invariants; rehearse downtime or dual-write/cutover design; validate reads/writes and rollback boundaries; then schedule cutover with backups and a tested recovery plan. Do not add the PostgreSQL driver until that work is authorized.


