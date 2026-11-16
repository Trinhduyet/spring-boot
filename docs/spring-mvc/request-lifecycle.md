# Spring MVC request lifecycle

An HTTP request enters the servlet container, passes through servlet filters (including Spring Security), and reaches `DispatcherServlet`. Handler mappings select a controller method. Argument resolvers bind path/query/body values; Jackson reads JSON; Bean Validation runs for `@Valid`. The controller calls a service, which calls repositories within transaction boundaries. The return value is converted to JSON and an HTTP response. Exceptions pass to `@ControllerAdvice` handlers.

```java
@GetMapping("/{id}")
ResponseEntity<ProductResponse> get(@PathVariable long id) {
    return ResponseEntity.ok(service.get(id));
}
```

ASP.NET Core has a related pipeline: middleware, routing, model binding/validation, controller/action filters, action, result execution. Spring's `DispatcherServlet` is a front controller; servlet filters and MVC interceptors have different positions and responsibilities than ASP.NET middleware.

**Pitfalls:** don't put business rules in the controller; don't return JPA entities; distinguish malformed JSON (binding error), invalid fields (validation error), missing resources (404), and unexpected failures (500).

**Lab:** send one valid and one invalid request to Product API; follow both through filter, binding, validation, controller, service, and exception advice. Source: [ProductController](../../src/main/java/com/example/catalog/product/api/controller/ProductController.java), [ApiExceptionHandler](../../src/main/java/com/example/catalog/api/error/ApiExceptionHandler.java).


