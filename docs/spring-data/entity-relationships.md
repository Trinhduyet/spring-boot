# Entity relationships and fetch behavior

JPA provides `@OneToOne`, `@OneToMany`, `@ManyToOne`, and `@ManyToMany`. The owning side controls the foreign-key update. Collections are often lazy; eager relationships can create large object graphs. This catalog uses an explicit join entity for product-category links rather than exposing a raw collection relationship.

```java
@ManyToOne(fetch = FetchType.LAZY)
private OrderEntity order;
```

EF Core navigation properties and `Include` express related ideas. In JPA use fetch joins, entity graphs, or projections for a use case instead of changing every relationship to eager. A common N+1 pattern is one parent query plus one query for each parent's lazy collection.

**Pitfalls:** lazy initialization after transaction close; bidirectional serialization cycles; cascades that delete more than intended; N+1 queries.

**Lab:** inspect order and order-line mapping and describe which side owns the foreign key. Source: [OrderEntity](../../src/main/java/com/example/catalog/ordering/infrastructure/persistence/entity/OrderEntity.java), [OrderLineEntity](../../src/main/java/com/example/catalog/ordering/infrastructure/persistence/entity/OrderLineEntity.java).


