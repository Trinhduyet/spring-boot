# Transaction behavior: EF Core and Spring

In EF Core, `SaveChanges` commonly defines the unit that persists tracked changes. Spring Data JPA repositories share a persistence context within the service transaction; use `@Transactional` on the application workflow so inventory reservation, order lines, and audit records commit or roll back together.

Spring's default proxy mode intercepts external method calls. Self-invocation bypasses transactional advice. Runtime exceptions roll back by default; checked exceptions do not unless configured. Do not catch and suppress persistence failures. Keep network calls out of database transactions and use outbox/idempotency patterns when a workflow includes external side effects.

This codebase uses SQL Server constraints and row locking for inventory correctness. Validate transaction behavior with SQL Server integration tests, not only mocks.


