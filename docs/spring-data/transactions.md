# Spring transactions

`@Transactional` usually opens/joins a transaction when a public method is invoked through the Spring proxy. Default propagation is `REQUIRED`; unchecked exceptions trigger rollback. `readOnly=true` communicates intent to the provider. Isolation determines visibility/concurrency semantics supported by the database. Optimistic locking uses `@Version`; pessimistic locking asks the provider/database for a write lock.

```java
@Transactional(rollbackFor = IOException.class)
public void operation() throws IOException { /* atomic work */ }
```

EF Core offers explicit transaction APIs and `SaveChanges` units. Spring's annotation is declarative and proxy-based; self-invocation bypasses it. H2 can demonstrate ordinary rollback but not prove SQL Server lock/isolation behavior.

**Pitfalls:** swallowed exceptions, assuming a nested method opens a new transaction, assuming flush commits, and relying on annotation on an internal self-call.

**Lab:** run the order rollback test, then identify a scenario requiring SQL Server verification. Source: [OrderService](../../src/main/java/com/example/catalog/ordering/application/service/OrderService.java), [OrderingTransactionTest](../../src/test/java/com/example/catalog/ordering/application/service/OrderingTransactionTest.java).


