# Bean lifecycle and circular dependencies

Spring reads bean definitions, resolves constructor dependencies, instantiates a bean, invokes injection/aware callbacks, calls `BeanPostProcessor` before initialization, then initialization methods such as `@PostConstruct`. Post-initialization processors can return a proxy. Managed singleton beans receive shutdown callbacks such as `@PreDestroy`.

```java
@Component
final class CacheWarmup {
    @PostConstruct void initialize() { /* validate or prepare small local state */ }
    @PreDestroy void close() { /* release an owned resource */ }
}
```

This resembles ASP.NET service construction and `IHostedService` startup/shutdown, but `BeanPostProcessor` can wrap a bean before consumers receive it. Constructor cycles fail because neither dependency can be built first. Break the cycle by clarifying ownership or extracting shared behavior rather than hiding it with field injection or `@Lazy`.

**Pitfalls:** avoid database-heavy work in `@PostConstruct`; don't expect prototype destruction to be container-managed; don't use lifecycle hooks for business workflows.

**Lab:** observe constructor, init, and destroy order in a small context test. Source: [CatalogApplication](../../src/main/java/com/example/catalog/CatalogApplication.java).


