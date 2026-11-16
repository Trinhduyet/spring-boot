# Spring MVC REST controllers

`@RestController` marks a controller whose return values are written to the response body. `@RequestMapping` defines a shared path; method mappings select HTTP verbs. `@PathVariable` binds path segments, `@RequestParam` binds query values, and `@RequestBody` deserializes JSON. Add `@Valid` to trigger Jakarta Bean Validation. Use `ResponseEntity` when status or headers matter.

```java
@PostMapping
ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
    var created = service.create(request);
    return ResponseEntity.created(URI.create("/api/v1/products/" + created.id())).body(created);
}
```

This maps to ASP.NET Core route attributes, model binding, `[FromBody]`, and `ActionResult<T>`. Spring's `ResponseEntity<T>` combines body, status, and headers. Keep controllers thin; delegate rules to a service and return DTOs rather than entities.

**Pitfalls:** avoid method-name routing, unbounded query parameters, and returning a JPA entity whose lazy state may escape the transaction. Use correct status codes and validate paging bounds.

**Lab:** compare `ProductController` with an ASP.NET Core controller for create/list endpoints; verify the POST 201 and `Location` header in MockMvc. Source: [ProductController](../../src/main/java/com/example/catalog/product/api/controller/ProductController.java), [ProductControllerTest](../../src/test/java/com/example/catalog/product/api/controller/ProductControllerTest.java).


