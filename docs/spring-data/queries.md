# JPQL, repositories, pagination, and N+1

Spring Data can derive queries from method names, run JPQL with `@Query`, or use native SQL when provider-specific behavior is truly needed. `Pageable` carries page, size, and sort; a `Page` also executes a count query. Projections load only selected fields. JPQL operates on entities/fields, not table/column names.

```java
Page<ProductEntity> search(String query, Pageable pageable);
```

EF Core LINQ over `IQueryable` is translated by its provider; JPQL is translated by Hibernate. Java Stream API processes already-loaded Java values in memory. `Include` maps roughly to a fetch join/entity graph, but SQL shape and duplicate handling differ.

**Pitfalls:** unbounded `findAll`, unvalidated page size, count-query cost, N+1 lazy loading, and using native SQL without documenting vendor dependence.

**Lab:** add a sort to a paged search and observe both content and count SQL; compare with EF Core `Skip/Take`. Source: [ProductRepository](../../src/main/java/com/example/catalog/product/infrastructure/persistence/repository/ProductRepository.java), [ProductRepositoryTest](../../src/test/java/com/example/catalog/product/infrastructure/persistence/repository/ProductRepositoryTest.java).


