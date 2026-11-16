# Migration strategy

Flyway scripts describe candidate schema changes; Hibernate only validates. Flyway is disabled by default (`DB_FLYWAY_ENABLED=false`). Enable it only for an explicitly reviewed schema/migration target. Review each migration with the database owner and inspect the current schema/history before any use against an existing SQL Server. Never automate a baseline against a database whose schema and data have not been inspected, drop/recreate existing tables, or edit an applied migration.

For existing data, inventory constraints, duplicates, nulls and row counts first. Reconcile data in bounded, restartable scripts with before/after counts and a restore plan. Use expand/migrate/contract for breaking changes. Coordinate Flyway permissions with the DBA; runtime users should not receive broad server administration rights.


