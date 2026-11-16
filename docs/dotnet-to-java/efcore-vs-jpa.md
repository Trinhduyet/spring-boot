# EF Core and Spring Data JPA / Hibernate

## Core mapping

| EF Core | JPA / Hibernate | Practical difference |
|---|---|---|
| `DbContext` | `EntityManager` plus persistence context | Both track managed entities; Spring typically scopes the persistence context to a transaction/request integration |
| Change Tracker | Persistence context | Managed entity changes are detected by dirty checking |
| `SaveChanges()` | `flush()` then transaction commit | Flush sends SQL; commit completes the database transaction. Flush is not commit |
| `Include` | fetch join, entity graph, projection | Choose fetch per use case; default relationships should avoid accidental eager graphs |
| LINQ over `IQueryable` | JPQL / Criteria / repository query | Both translate supported expressions to SQL; Java Stream runs in memory |
| `AsNoTracking()` | no exact direct equivalent | `readOnly=true` is a transaction/provider hint, not a universal tracking switch |
| EF transaction | Spring `@Transactional` | Spring annotations are proxy-based; self-invocation bypasses advice |
| EF migrations | Flyway | Flyway scripts are explicit SQL and independent of entity model generation |

## Example

```java
@Transactional(readOnly = true)
public ProductResponse get(long id) {
    var entity = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    return mapper.toResponse(entity);
}
```

```csharp
public async Task<ProductResponse> Get(long id, CancellationToken ct) {
    var entity = await db.Products.AsNoTracking().SingleOrDefaultAsync(x => x.Id == id, ct)
        ?? throw new ProductNotFoundException(id);
    return mapper.ToResponse(entity);
}
```

Both fetch one row and map it to an API contract. The Java read-only transaction does not promise the same no-tracking behavior as EF `AsNoTracking`. JPA's persistence context identity map can return the same managed entity instance within its scope. Dirty checking may issue an update at flush even without an explicit save call. `EntityManager.flush()` synchronizes pending state but does not commit; Spring transaction completion commits or rolls back.

## Relationships, queries, and transactions

`@ManyToOne` and `@OneToMany` describe relationships; `LAZY` is a safer default for collections. Accessing a lazy relationship after the persistence context closes can fail. Fetch joins and entity graphs control needed data; projections can avoid loading full entities. N+1 happens when a query loads parent rows and then issues per-parent relationship queries. EF `Include` and JPA fetch plans solve related problems but differ in SQL generation and tracking details.

Spring Data repository methods and JPQL execute database queries. `products.stream().filter(...)` only processes already-loaded Java objects. EF `IQueryable` similarly must remain unmaterialized until a provider translates the expression.

Spring `@Transactional` usually enters through a Spring proxy. Self-invocation bypasses transaction advice; default propagation joins the current transaction; unchecked exceptions roll back by default. Test the observable rollback behavior with H2 for fast learning, but do not infer SQL Server lock, collation, migration, or dialect compatibility from H2.

## Pitfalls and lab

Avoid serializing entities directly, accidental eager loading, leaking lazy proxies into controllers, large unbounded result lists, and treating flush as commit. Review actual SQL when learning fetch behavior.

**Lab:** compare `ProductRepository.search` with Java Stream filtering and an EF `IQueryable` equivalent; add a paged result test, then describe which execution environment runs each predicate. Source: [ProductRepository](../../src/main/java/com/example/catalog/product/infrastructure/persistence/repository/ProductRepository.java), [ProductRepositoryTest](../../src/test/java/com/example/catalog/product/infrastructure/persistence/repository/ProductRepositoryTest.java), [OrderService](../../src/main/java/com/example/catalog/ordering/application/service/OrderService.java).


