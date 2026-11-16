# Bean scopes and lifecycle

## Scopes compared with ASP.NET Core

| Spring | Meaning | Rough .NET comparison | Important difference |
|---|---|---|---|
| singleton (default) | one per ApplicationContext | `AddSingleton` | Context lifetime, not necessarily process lifetime |
| prototype | new instance when requested from container | `AddTransient` | Spring does not track prototype destruction; direct injection into a singleton captures one instance |
| request | one per HTTP request | `AddScoped` in web apps | Servlet scope; may require a scoped proxy when injected into singleton |
| session | one per HTTP session | no direct default equivalent | Web-session state has different lifetime and scaling implications |

Prefer stateless singleton services. Use request scope only when a dependency truly belongs to a request. A singleton depending on a prototype does not receive a fresh object on every method call; use `ObjectProvider<T>` or a scoped proxy only when that lifecycle is intentional.

## Lifecycle stages

1. Spring reads bean definitions and resolves constructor dependencies.
2. The object is instantiated and dependency injection/aware callbacks run.
3. `BeanPostProcessor` before-initialization callbacks run.
4. Initialization callbacks run: `@PostConstruct`, `InitializingBean`, or a configured init method.
5. Post-initialization processors may return a proxy instead of the raw bean.
6. At context shutdown, managed singleton beans receive destruction callbacks such as `@PreDestroy`.

```java
@Component
final class Warmup {
    @PostConstruct void initialize() { /* validate local startup state */ }
    @PreDestroy void close() { /* release owned resource */ }
}
```

Use lifecycle hooks for small resource setup/cleanup, not business workflows. Constructor injection cycles are rejected. Break cycles by clarifying ownership or extracting a collaborator. `@Lazy` and setter injection can hide a cycle rather than resolve the underlying dependency design.

**Lab:** create a bean with constructor, `@PostConstruct`, and `@PreDestroy` log points; start and close a test context and observe order. Source: [CatalogApplication](../../src/main/java/com/example/catalog/CatalogApplication.java).


