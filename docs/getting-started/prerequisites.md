# Prerequisites

Install JDK 21. Maven is supplied by the Maven Wrapper. A Docker Engine with Compose is optional for the API container exercise. SQL Server access is needed only to run the application against the existing development database; default tests use H2 and need no external service.

```powershell
java -version
docker version
docker compose version
```

For direct local execution, set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` in the environment and start the application with the `local` profile. The `.env.example` values are placeholders for local Compose; replace them locally and never commit actual credentials.

**Lab:** run `./mvnw clean test` with Docker stopped and verify all default tests pass. Source: [test profile](../../src/main/resources/application-test.yml).


