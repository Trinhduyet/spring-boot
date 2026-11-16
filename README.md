# Spring Boot learning API for senior .NET developers

A Java 21 / Spring Boot REST API for learning framework behavior through a compact catalog example. Source is organized by business feature with `api`, `application`, `domain`, and `infrastructure` packages where needed. Within each feature, controllers, request/response DTOs, application inputs/results, services, ports, mappers, entities and repositories have distinct responsibility packages. See [the current folder tree](docs/architecture/project-structure.md) and [ADR 0005](docs/adr/0005-responsibility-packages.md). SQL Server is the development database; the `test` profile uses H2 and requires no Docker.

## Quick start

Requirements: JDK 21 and the Maven Wrapper. Tests do not need a database service.

```powershell
.\mvnw.cmd clean test
```

```bash
./mvnw clean test
```

For local API use, configure `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`, then run with the `local` profile. Local profile intentionally disables authentication for isolated development. Do not expose it on a shared network. Runtime uses `ddl-auto=validate`; compare mappings with the actual database schema before connecting. Flyway is disabled by default and should be explicitly enabled only after migration review and approval. Never use `ddl-auto=create`/`update` against existing data.

## Learn in this order

Start with the Vietnamese [project, Java core, Spring and annotations guide](docs/huong-dan-java-spring.md) for a walkthrough grounded in this repository and comparisons with .NET.

1. [14-day learning roadmap](docs/dotnet-to-java/roadmap-14-days.md)
2. [Java 21 for C# developers](docs/java-core/java-for-csharp-developers.md)
3. [Spring IoC, scopes, lifecycle and AOP](docs/spring-core/ioc-di.md)
4. [REST request flow](docs/spring-mvc/request-lifecycle.md)
5. [JPA and EF Core comparison](docs/dotnet-to-java/efcore-vs-jpa.md)
6. [Testing guide](docs/development/testing-guide.md)
7. [Reference project review](docs/architecture/reference-project-review.md)

The source of truth is executable code and focused tests. [Architecture overview](docs/architecture/overview.md), [project structure](docs/architecture/project-structure.md), [layer responsibilities](docs/architecture/layered-architecture.md), and [dependency rules](docs/architecture/dependency-rules.md) explain feature/layer boundaries. Repository guidance is in [AGENTS.md](AGENTS.md); learning workflows are in `.agents/skills/`.

## API

Product and category endpoints are under `/api/v1`. OpenAPI UI is available at `/swagger-ui/index.html` when running locally. Product listing supports search, sorting, and pagination; DTO validation and consistent API errors are implemented at the HTTP boundary.

The primary learning path is Product and Category. The repository also retains inventory, order, and audit examples for transaction and persistence exercises.

The `dev` profile demonstrates HTTP Basic with `APP_USERNAME` and `APP_PASSWORD`; it is an in-memory learning example, not production identity management. Local/test profiles are isolated and permit requests.

## Database and test scope

Runtime development connects to the existing SQL Server through environment variables. H2 is used only for fast tests. H2 does not prove SQL Server dialect, locking, collation, migration, or constraint compatibility; those behaviors remain unverified until exercised against an authorized SQL Server environment.

Default tests run with `./mvnw clean test`; `./mvnw verify` also creates the JaCoCo report and enforces the configured coverage gate. The suite includes JUnit 5, Mockito, AssertJ, Spring Boot test support, and H2 repository/context tests. No Testcontainers or external SQL Server is required.

## Basic Docker and CI

`Dockerfile`, `docker-compose.yml`, and `.env.example` demonstrate running the API against an existing SQL Server. `.gitlab-ci.yml` has only validate, test, build, docker-build, and docker-push stages. Registry publishing requires a GitLab runner with Docker build capability. This starter does not include production deployment automation, a monitoring stack, Kubernetes, or Helm.

