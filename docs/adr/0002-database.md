# ADR 0002: SQL Server first and Flyway

- Status: Accepted
- Decision: Microsoft SQL Server is the current system of record. Flyway SQL Server migrations own approved schema changes; Hibernate validates mappings and does not update production schemas.
- Consequences: the default learning suite uses H2, which does not establish SQL Server dialect, locking, collation, or migration compatibility. SQL Server-specific behavior needs verification against an approved development SQL Server.


