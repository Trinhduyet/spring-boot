# Persistence context and entity states

JPA entities may be new, managed, detached, or removed. A managed entity is tracked by the persistence context; identity lookup returns the same managed object for a row within that context. Dirty checking detects changes. `flush()` sends pending SQL but the transaction can still roll back; commit completes the transaction.

```java
entityManager.flush(); // synchronize pending changes; not a commit
```

This resembles EF Core `ChangeTracker`, but the Spring transaction often defines the EntityManager scope. EF Core commonly calls `SaveChanges`; JPA may flush automatically before a query or transaction completion. Spring `readOnly=true` is a hint, not equivalent to EF `AsNoTracking`.

**Pitfalls:** detached object edits are not automatically tracked; flush is not commit; lazy loads need an open persistence context.

**Lab:** modify a managed entity, flush, then throw and verify rollback through [OrderingTransactionTest](../../src/test/java/com/example/catalog/ordering/application/service/OrderingTransactionTest.java). Source: [ProductEntity](../../src/main/java/com/example/catalog/product/infrastructure/persistence/entity/ProductEntity.java).


