# Future SQL Server to PostgreSQL path

The `postgres` profile now supports a separate PostgreSQL database using its own JDBC driver and Flyway migrations. The existing SQL Server configuration and migrations remain available. See [PostgreSQL and Railway setup](postgresql-railway.md); scripts for one vendor must not be applied to the other.

Changing the connection does not copy SQL Server data. For an actual data migration, inventory and reconcile existing data, keys, decimals and timestamps; review collation and locking differences; rehearse cutover and rollback with an appropriate backup. H2 tests do not establish SQL Server or PostgreSQL equivalence.


