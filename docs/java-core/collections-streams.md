# Collections and Stream API vs LINQ

Use `List` for ordered duplicates, `Set` for uniqueness, and `Map` for key lookup. `ArrayList`, `HashSet`, and `HashMap` are common implementations; `List.of` creates a fixed, unmodifiable collection. Arrays have fixed length and can store primitives; generic collections use wrapper types such as `int` → `Integer`.

```java
var names = products.stream().filter(ProductResponse::active)
        .map(ProductResponse::name).distinct().toList();
```
```csharp
var names = products.Where(p => p.Active).Select(p => p.Name).Distinct().ToList();
```

Both APIs support fluent transformations, but Java `Stream` processing is in-memory and single-use. C# `IEnumerable` is also in-memory, while EF Core `IQueryable` builds a database expression. Use Spring Data/JPQL for database filtering; calling `.stream()` on loaded entities cannot push a predicate to SQL. `filter` keeps items, `map` transforms each, `flatMap` flattens nested streams, and `Collectors` builds results. `Optional<T>` represents a possibly absent return value, not a general replacement for nullable fields or parameters.

**Pitfalls:** terminal operations consume a stream; `toList()` is unmodifiable; `Collectors.toMap` needs a duplicate-key strategy; avoid side effects in parallel streams.

**Lab:** compare filtering all loaded products with `ProductRepository.search`; inspect which work happens in Java vs SQL. Source: [ProductRepository](../../src/main/java/com/example/catalog/product/infrastructure/persistence/repository/ProductRepository.java).


