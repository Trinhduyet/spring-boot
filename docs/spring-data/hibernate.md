# Hibernate provider behavior

Hibernate implements JPA and translates entity operations/JPQL to SQL for the selected dialect. It manages entity state in a persistence context and flushes changes at synchronization points. SQL Server and H2 differ in SQL syntax, types, collation, and locking behavior.

```java
@Transactional
public void rename(long id, String name) {
    ProductEntity product = repository.findById(id).orElseThrow();
    product.update(name, product.getPrice(), product.isActive());
    // A managed entity is detected by dirty checking at flush/commit.
}
```

EF Core also tracks changes in a unit of work, but its explicit `SaveChanges` usage is different from Hibernate dirty checking. H2 tests validate only the behavior exercised with H2.

**Pitfalls:** inspect generated SQL for unexpected updates and N+1 selects; do not treat provider defaults as portable SQL guarantees.

**Lab:** update an entity inside a transaction and observe SQL at flush; compare with EF `SaveChanges`. Source: [ProductService](../../src/main/java/com/example/catalog/product/application/service/ProductService.java).


