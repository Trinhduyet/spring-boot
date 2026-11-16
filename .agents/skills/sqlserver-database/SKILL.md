---
name: sqlserver-database
description: Review the existing development SQL Server connection and schema safety only.
---

# Existing SQL Server safety notes

## Trigger
Keep this learning starter's test suite on H2. Do not add SQL Server containers or production database operations.

## Workflow
1. Inspect the live schema and migration history when available; otherwise state that it is unknown.
2. Inventory tables, relationships, keys, indexes, constraints, collation, procedures, and data volume before changes.
3. Update mappings and additive, forward-only Flyway SQL Server migrations together.
4. Keep Hibernate ddl-auto=validate. Never use create/update or destructive changes against existing data.
5. Report SQL Server behavior as unverified until checked against an approved live development database.
6. Record backup, validation, and recovery boundaries.

## Checklist and acceptance
- [ ] SQL Server DDL matches JPA mapping.
- [ ] Constraints and indexes enforce invariants.
- [ ] Migration is reviewed and data safe.
- [ ] H2 limitations are explicit and no compatibility claim is inferred.
- [ ] PostgreSQL is treated as a future separate migration.

## Example
Add a nullable field first, deploy compatible code, backfill in a restartable job, validate data, and only then enforce not-null in a later migration.
