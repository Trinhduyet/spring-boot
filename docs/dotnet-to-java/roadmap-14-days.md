# 14-day Java and Spring learning roadmap

Plan 2–3 focused hours per day. Each day ends with a small code artifact or a written explanation tied to this project.

| Day | Focus | Code lab and outcome |
|---|---|---|
| 1 | Java classes, interfaces, access, constructors | Model a small immutable value and explain Java access levels vs C# |
| 2 | Collections and arrays | Compare `List`, `Set`, `Map`, arrays, and immutable factories |
| 3 | Lambdas, Streams, Optional, generics, exceptions, records | Implement an in-memory stream and contrast it with repository SQL search |
| 4 | JVM, threads, executors, CompletableFuture, virtual threads | Run a bounded blocking-task experiment and explain the JVM memory model basics |
| 5 | Spring IoC, ApplicationContext, bean definitions, constructor DI | Trace ProductController → ProductService → ProductRepository |
| 6 | Scopes, lifecycle, auto-configuration, AOP | Explain singleton vs scoped lifetime and demonstrate proxy self-invocation |
| 7 | Spring MVC and HTTP lifecycle | Trace request mapping, argument binding, validation, controller, response |
| 8 | DTOs, validation, exception handling, pagination | Add a request validation case and verify its HTTP error |
| 9 | JPA entities, repositories, relationships, entity states | Compare EntityManager/persistence context with DbContext/ChangeTracker |
| 10 | JPQL, fetch plans, N+1, dirty checking, flush | Inspect a query and explain Stream vs JPQL/EF `IQueryable` |
| 11 | Transactions, propagation, rollback, isolation, optimistic locking | Run the H2 transaction rollback test and explain SQL Server-specific limits |
| 12 | JUnit, Mockito, AssertJ, MockMvc, H2, JaCoCo | Add a focused service or repository test; run `./mvnw clean test` |
| 13 | Maven lifecycle, Dockerfile, Compose | Package the JAR and run the API container against an existing SQL Server |
| 14 | GitLab CI and architecture review | Trace validate/test/build/image stages and explain each package boundary |

**Outcome:** explain why each framework layer exists, where state lives, which operations run in memory vs SQL, and what evidence H2 tests provide. Source links: [Product API](../../src/main/java/com/example/catalog/product), [Spring Core notes](../spring-core/ioc-di.md), [testing guide](../development/testing-guide.md).


