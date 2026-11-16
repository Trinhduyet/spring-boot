---
name: spring-rest-api
description: Design Spring MVC endpoints, DTO validation, errors, pagination, and MockMvc tests.
---

# Spring REST API

## Trigger
Use when adding or changing a Spring MVC endpoint or its HTTP contract.

## Workflow
1. Define route, DTO, validation, status codes, and error contract.
2. Keep controller HTTP concerns separate from service rules.
3. Keep DTOs independent of JPA entities; use records for immutable values.
4. Test success and invalid/not-found behavior with MockMvc or service tests.

```java
@PostMapping
ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
}
```

## Checklist
- [ ] Validation occurs at the request boundary.
- [ ] Correct status, location, and error details are returned.
- [ ] Paging/sorting inputs are bounded.
- [ ] Tests cover HTTP contract and service rules.

## Check
Run focused tests, then `./mvnw clean test`.
