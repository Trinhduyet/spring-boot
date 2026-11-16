---
name: java-testing
description: Legacy alias; use java-unit-testing for lightweight JUnit, Mockito, MockMvc, and H2 workflows.
---

# Java test workflow

Follow [java-unit-testing](../java-unit-testing/SKILL.md). Default suite uses H2 and must run without Docker. H2 results do not establish SQL Server compatibility.

## Checklist

- [ ] Assertions cover useful outcomes, not merely method calls.
- [ ] No sleep-based race test.
- [ ] H2 is used for fast repository learning tests; SQL Server-specific behavior is labeled unverified.
- [ ] Test data and containers are isolated and cleaned.
- [ ] Test failure does not leak sensitive data.

References: `docs/development/testing-guide.md`, `ProductRepositoryTest`.

