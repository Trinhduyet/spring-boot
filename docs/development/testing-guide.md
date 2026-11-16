# Testing guide

The default suite runs locally with no Docker daemon, SQL Server, or external identity provider. It uses JUnit 5, Mockito, AssertJ, Spring Boot Test, MockMvc where appropriate, H2 for repository/context tests, and JaCoCo.

```bash
./mvnw clean test
```

```powershell
.\mvnw.cmd clean test
```

Use plain unit tests for business rules, MockMvc for HTTP binding/validation/errors, and `@DataJpaTest` for repository queries. The test profile uses an in-memory H2 database and `create-drop` only inside that disposable test database; Flyway is disabled for tests because the application migrations target SQL Server. Do not interpret H2 success as SQL Server compatibility evidence.

`ProductRepositoryTest` demonstrates search and paging on H2. `OrderingTransactionTest` demonstrates a business transaction rollback on H2. SQL Server locking, collation, migration, and dialect details require separate verification against the approved SQL Server environment and are not covered by this default test input.

Inspect `target/surefire-reports/` for JUnit results and `target/site/jacoco/index.html` after `./mvnw verify` for coverage. Tests should assert observable results and state, not only implementation calls.

**Lab:** introduce a repository filter, test empty and matching pages, then add a MockMvc test for invalid input. Compare the same filtering behavior with a C# LINQ query and an EF Core `IQueryable` query. Source links: [ProductRepositoryTest](../../src/test/java/com/example/catalog/product/infrastructure/persistence/repository/ProductRepositoryTest.java), [ProductServiceTest](../../src/test/java/com/example/catalog/product/application/service/ProductServiceTest.java).


