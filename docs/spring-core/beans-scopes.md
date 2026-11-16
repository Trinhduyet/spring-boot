# Bean scopes and .NET lifetime comparison

Spring singleton is one object per `ApplicationContext` and is the default. Prototype creates a new instance when the container is asked for one; Spring does not manage its destruction callback. Request scope is one object per servlet request; session scope follows an HTTP session.

| Spring | Approximate .NET mapping | Difference |
|---|---|---|
| singleton | `AddSingleton` | Context lifetime, not guaranteed process lifetime |
| prototype | `AddTransient` | Direct injection into a singleton captures one instance |
| request | `AddScoped` | Servlet scope and proxy rules differ |
| session | no exact default match | HTTP session state has separate scaling implications |

```java
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
class RequestContext { }
```

Prefer stateless singleton services. A singleton that needs a fresh prototype per call should use an explicit provider; do not make the singleton mutable to store request data.

**Pitfalls:** scope names are not 1:1 portable; singleton-to-request injection needs a scoped proxy/provider; session scope can retain user data longer than expected.

**Lab:** create a context and compare two lookups of singleton and prototype beans. Source: [ProductService](../../src/main/java/com/example/catalog/product/application/service/ProductService.java).


