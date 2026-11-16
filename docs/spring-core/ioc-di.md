# IoC, dependency injection and auto-configuration

## Container and bean definitions

Spring's `BeanFactory` resolves and creates beans; `ApplicationContext` adds application events, resources, environment/property sources and integration conveniences. A `BeanDefinition` describes a bean before instantiation. `@SpringBootApplication` combines configuration, component scanning from its package downward, and auto-configuration. Put the application class at the root package so feature packages are discovered.

```java
@Service
public class ProductService {
    private final ProductRepository repository;
    public ProductService(ProductRepository repository) { this.repository = repository; }
}

@Configuration
class ClockConfiguration {
    @Bean Clock clock() { return Clock.systemUTC(); }
}
```

`@Component` marks a general scanned bean; `@Service` communicates application logic; `@Repository` identifies persistence components and participates in exception translation. `@Configuration` declares bean methods. `@Bean` is useful for third-party types and explicit construction. Constructor injection makes required dependencies visible and supports plain unit tests. ASP.NET Core has a registration container (`AddScoped`, etc.); Spring discovers stereotype beans and also supports explicit `@Bean` methods.

## Scopes and lifecycle

Spring singleton means one bean instance per `ApplicationContext`, not one per process. Prototype creates an instance per lookup/injection request; the container does not manage its full destruction lifecycle. Request and session scopes bind instances to servlet web scopes. They are not exact equivalents of ASP.NET `AddSingleton`, `AddTransient`, and `AddScoped`: Spring's default singleton and web-scoped proxies/lifecycle have different resolution semantics. Never store per-request mutable state in a singleton.

Creation broadly resolves dependencies, invokes constructors, performs injection/aware callbacks, runs `BeanPostProcessor` before-init hooks, initialization callbacks (`@PostConstruct`, `InitializingBean`, custom init method), then post-init hooks (where proxies may be wrapped). Destruction can invoke `@PreDestroy` and configured callbacks for managed singletons. `BeanPostProcessor` is the extension point behind many proxy features.

Circular constructor dependencies fail because neither object can be constructed first. Treat that as a design signal; split shared behavior or move orchestration to a third service. Setter/field cycles may sometimes be resolvable but hide coupling and are discouraged.

## Auto-configuration and AOP

Spring Boot starters bring related dependencies; conditional auto-configuration contributes defaults when classes/properties/beans match. User-defined beans can replace or customize those defaults. Inspect the condition evaluation report (`--debug`) and application context rather than assuming a starter means a feature is configured.

Spring AOP is proxy-based. JDK dynamic proxies implement interfaces; CGLIB creates subclasses for concrete classes. Calls that cross the proxy can be intercepted for `@Transactional`, caching, or method security. A method calling another method on `this` bypasses the proxy (self-invocation), so the inner annotation does not start a new transaction. Public service methods are the usual transaction boundary.

```java
@Service
class TransferService {
    @Transactional
    public void transfer(long from, long to) { /* one atomic use case */ }
}
```

ASP.NET middleware/filters and decorators are related interception tools, but Spring AOP is generally method-proxy interception, not an ambient request pipeline. Transaction annotation semantics depend on proxy entry, propagation, and exception type. Runtime exceptions roll back by default; configure `rollbackFor` when checked exceptions should roll back.

**Pitfalls:** don't use field injection, don't annotate every method reflexively, and don't expect self-invocation to trigger proxy behavior. Avoid manually constructing a Spring-managed service with `new` in application code.

**Lab:** add a `Clock` bean and inject it through a constructor; test two beans receive the same singleton. Create a transactional public use case and describe where proxy invocation begins. Source: [CatalogApplication](../../src/main/java/com/example/catalog/CatalogApplication.java), [ProductService](../../src/main/java/com/example/catalog/product/application/service/ProductService.java), [SecurityConfig](../../src/main/java/com/example/catalog/security/infrastructure/config/SecurityConfig.java).


