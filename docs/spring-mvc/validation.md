# Spring MVC validation

Place constraints on request DTO fields and mark the controller body `@Valid`. Validation runs after JSON binding and before controller invocation; violations are raised as `MethodArgumentNotValidException` and translated by the global advice.

```java
public record CreateProductRequest(
        @NotBlank @Size(max = 64) String sku,
        @NotBlank @Size(max = 200) String name,
        @NotNull @DecimalMin("0.00") BigDecimal price,
        boolean active) { }
```

ASP.NET Core uses DataAnnotations and model validation with its own automatic API-controller behavior. Both keep input constraints near the request model, but exception types and response shaping are framework-specific.

**Pitfalls:** bean constraints do not replace business validation or database constraints; avoid putting persistence entities in the request; validate ranges and lengths at the boundary.

**Lab:** test missing name and negative price using MockMvc, then compare the response with ASP.NET Core's model-state problem details. Source: [CreateProductRequest](../../src/main/java/com/example/catalog/product/api/request/CreateProductRequest.java), [ProductController](../../src/main/java/com/example/catalog/product/api/controller/ProductController.java).


