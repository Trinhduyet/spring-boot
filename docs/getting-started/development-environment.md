# Development environment

Use Java 21 and the Maven Wrapper. Run `./mvnw clean test` (Windows: `./mvnw.cmd clean test`) to compile and run the default H2 test suite without SQL Server or Docker.

For local API use, create a local `.env` from `.env.example` and set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` for an approved development database. `docker compose up --build` runs only the API and connects to that existing SQL Server; it does not create a database. For IDE execution, export those variables and select the `local` Spring profile.

**Lab:** run tests offline first, then start the local API and call `GET /api/v1/products`. Source: [Compose](../../docker-compose.yml), [test guide](../development/testing-guide.md).


