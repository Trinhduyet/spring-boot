---
name: spring-data-jpa
description: Implement Hibernate entities, Spring Data repositories, fetch plans, transactions, and fast H2 learning tests.
---

# JPA workflow

1. Inspect table constraints, entity mappings and query callers together.
2. Design a forward Flyway migration; never rely on Hibernate schema update.
3. Map exact SQL types, indexes, nullability, foreign keys and delete behavior.
4. Keep associations lazy and map DTOs inside a defined transaction; inspect N+1 risk.
5. Choose optimistic versioning or a row lock/atomic conditional update from the invariant.
6. Use H2 for fast repository tests and state that it does not prove SQL Server locking, collation, or migration behavior.
7. Check rollback and query behavior; label SQL Server-specific assumptions unverified.

## Checklist

- [ ] No entity crosses a feature boundary or API boundary.
- [ ] No unbounded collection query.
- [ ] Dirty checking and flush timing are understood.
- [ ] Migration supports rolling deployment where required.
- [ ] H2 limitations and SQL Server verification status are explicit.

References: `docs/development/transactions.md`, `docs/architecture/database-design.md`, `docs/dotnet-to-java/efcore-vs-jpa.md`.

