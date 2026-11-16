# Existing SQL Server development connection

The application can connect to an existing development SQL Server; it does not provision one. Supply `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` through environment variables or a local ignored `.env`. Keep credentials out of Git and logs.

The repository has no live schema snapshot or database credentials. Before running against a populated database, inspect its objects and migration history, compare them with the JPA mappings and Flyway scripts, and get database-owner approval. Never enable `ddl-auto=create` or `update` against an existing database.

Default tests use H2 with a disposable schema and do not contact SQL Server. H2 cannot prove SQL Server-specific types, collation, locking, SQL syntax, or migration behavior. These remain unverified until checked against an approved SQL Server environment.

**Lab:** configure a local connection using environment variables, run the API with the `local` profile, and compare one read query with the entity and migration definitions. Source: [application config](../../src/main/resources/application.yml), [SQL Server migrations](../../src/main/resources/db/migration).


