# REST exception handling

`@RestControllerAdvice` centralizes conversion from known application exceptions to stable HTTP errors. Map not-found to 404, validation to 400, conflict to 409, and unexpected exceptions to a generic 500 response without stack traces or secrets.

```java
@ExceptionHandler(ProductNotFoundException.class)
ResponseEntity<ApiError> productNotFound(ProductNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiError("PRODUCT_NOT_FOUND", ex.getMessage()));
}
```

ASP.NET Core can centralize errors with exception-handling middleware and ProblemDetails; Spring advice is controller-oriented and participates in MVC exception resolution. Keep public error contracts stable while logging diagnostic details internally.

**Pitfalls:** don't expose SQL messages, tokens, or stack traces; don't turn every exception into 400; preserve status distinctions and correlation identifiers.

**Lab:** add a MockMvc assertion for an unknown product and an invalid request; verify status, code, and that internals are absent. Source: [ApiExceptionHandler](../../src/main/java/com/example/catalog/api/error/ApiExceptionHandler.java), which currently returns Spring `ProblemDetail`.


