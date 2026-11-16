# Configuration

Spring property sources combine YAML, profile files, environment variables, input-line args and external config. SQL Server connection values use `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`; the `dev` HTTP Basic user uses `APP_USERNAME` and `APP_PASSWORD`. Keep secrets outside source. Flyway is disabled by default and can be explicitly enabled with `DB_FLYWAY_ENABLED=true` only after reviewing the target schema and migrations.

Validate config at startup using `@ConfigurationProperties` and bean validation as security settings mature. Do not copy ASP.NET's scoped service lifetime intuition to Spring singleton services; keep request data out of bean fields.


