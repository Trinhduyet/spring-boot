---
name: java-unit-testing
description: Add JUnit 5, Mockito, AssertJ, MockMvc, Spring Boot, and H2 tests without Docker dependencies.
---

# Java unit and slice testing

## Trigger
Use when adding or improving Java/Spring tests.

## Workflow
1. State the invariant and select unit, MVC slice, JPA slice, or context test.
2. Mock true collaborators only; assert useful outcomes with AssertJ.
3. Use `@DataJpaTest` with H2 and the test profile for app-context checks.
4. Keep tests local and deterministic; avoid sleeps and external services.

```java
@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest { /* repository behavior on H2 */ }
```

## Checklist
- [ ] Success and meaningful failure outcomes are asserted.
- [ ] Database is in-memory and isolated.
- [ ] H2 is not claimed as SQL Server compatibility proof.
- [ ] Default suite has no Testcontainers or Docker requirement.

## Check
Run `./mvnw clean test`; inspect Surefire and JaCoCo reports.
