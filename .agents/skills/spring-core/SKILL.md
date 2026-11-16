---
name: spring-core
description: Explain or modify Spring IoC, beans, scopes, lifecycle, auto-configuration, or proxy-based AOP.
---

# Spring Core

## Trigger
Use for changes to bean wiring, Spring configuration, lifecycle, scopes, or AOP.

## Workflow
1. Trace bean creation from `CatalogApplication`.
2. Prefer constructor injection and explicit configuration for third-party beans.
3. Explain proxy entry points for transactions and advice.
4. Compare scope semantics with ASP.NET Core registrations.

```java
@Service
final class CatalogLookup {
    private final ProductRepository products;
    CatalogLookup(ProductRepository products) { this.products = products; }
}
```

## Checklist
- [ ] Explain container/lifecycle behavior.
- [ ] Include Java example and .NET comparison.
- [ ] Cover proxy, scope, or lifecycle pitfalls.
- [ ] Include a lab and source link.

## Check
Run a focused Spring test or `./mvnw clean test`; inspect `--debug` condition reports for auto-configuration.
