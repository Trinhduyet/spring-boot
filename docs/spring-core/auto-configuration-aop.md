# Auto-configuration and proxy AOP

## Auto-configuration

`@SpringBootApplication` includes `@EnableAutoConfiguration`. Boot applies conditional configuration based on classpath, properties, and existing beans. For example, adding the MVC starter makes MVC auto-configuration eligible; application-defined beans may replace selected defaults. Starters are dependency bundles, not a promise that every feature is active.

Use `application.yml` and profile-specific files for environment settings. Bind groups of settings to a typed `@ConfigurationProperties` class and validate it when configuration is required. Use `@ConditionalOnProperty` for optional integrations. Run with `--debug` to inspect why auto-configurations matched or did not match.

## AOP proxies and transactions

Spring AOP wraps eligible beans with JDK interface proxies or CGLIB subclass proxies. The caller must invoke through the proxy for advice to execute. A direct `this.otherMethod()` call stays on the target object and bypasses advice. `@Transactional` therefore belongs on a method reached from another bean at a clear service/use-case boundary.

```java
@Service
class CheckoutService {
    private final OrderRepository orders;
    CheckoutService(OrderRepository orders) { this.orders = orders; }

    @Transactional
    public void placeOrder(CreateOrderRequest request) {
        // repository changes share this transaction
    }
}
```

The closest ASP.NET comparison is interception through proxies/decorators plus explicit transaction APIs; it is not the same as middleware. Spring defaults to `REQUIRED` propagation and rollback on unchecked exceptions. `readOnly=true` is a hint to transaction providers, not an EF Core `AsNoTracking` equivalent. Self-invocation does not change propagation because no proxy call occurs.

**Pitfalls:** private/final methods and classes can constrain proxying; annotations on methods that are never called through Spring are inert. Do not swallow exceptions inside a transactional method if rollback is required. Do not assume a read-only flag prevents writes at the database level.

**Lab:** demonstrate a transaction rollback after a runtime exception, then add a checked exception and set `rollbackFor` explicitly. Source: [OrderService](../../src/main/java/com/example/catalog/ordering/application/service/OrderService.java), [InventoryService](../../src/main/java/com/example/catalog/inventory/application/service/InventoryService.java).


