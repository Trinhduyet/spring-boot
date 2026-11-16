# Spring AOP proxy and `@Transactional`

Spring AOP usually uses a JDK dynamic proxy for interface-based beans or a CGLIB subclass proxy for concrete beans. The call must pass through the proxy for method advice to run. `this.otherMethod()` is self-invocation on the target and bypasses the proxy.

```java
@Service
class CheckoutService {
    @Transactional
    public void checkout(CreateOrderRequest request) { /* atomic use case */ }
}
```

This is related to .NET interception/decorators but is not ASP.NET middleware. Spring's default transaction propagation joins an existing transaction (`REQUIRED`); unchecked exceptions roll back by default. A checked exception needs `rollbackFor` if it should roll back. `readOnly=true` is a provider hint, not an EF Core `AsNoTracking` equivalent.

**Pitfalls:** an annotated method called only from another method on the same object gets no new proxy advice; private/final method/class constraints can prevent subclass interception; swallowed exceptions can prevent expected rollback.

**Lab:** trigger a runtime exception after a repository write and verify rollback in the H2 transaction example; then explain why a SQL Server lock claim needs SQL Server verification. Source: [OrderService](../../src/main/java/com/example/catalog/ordering/application/service/OrderService.java), [OrderingTransactionTest](../../src/test/java/com/example/catalog/ordering/application/service/OrderingTransactionTest.java).


