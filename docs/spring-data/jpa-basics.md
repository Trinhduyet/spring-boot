# JPA entity and repository basics

JPA is a persistence specification; Hibernate is the provider used here. An `@Entity` has identity and maps fields to a table. Spring Data JPA creates repository implementations from interfaces such as `JpaRepository<ProductEntity, Long>`.

```java
@Entity
@Table(name = "products")
class ProductEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
```

EF Core entities and `DbSet<T>` are similar at a high level, but JPA annotations and provider behavior differ. Keep request/response DTOs separate from entities; avoid returning managed objects from controllers.

**Pitfalls:** entities need stable identity and a protected no-arg constructor; generated SQL depends on provider/dialect; database constraints remain important.

**Lab:** trace entity annotations to the SQL Server migration and compare with an EF Core fluent configuration. Source: [ProductEntity](../../src/main/java/com/example/catalog/product/infrastructure/persistence/entity/ProductEntity.java), [ProductRepository](../../src/main/java/com/example/catalog/product/infrastructure/persistence/repository/ProductRepository.java).


