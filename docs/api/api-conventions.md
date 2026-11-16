# API conventions

Use `/api/v1`, nouns for resources and HTTP methods for operations. Create returns 201 with `Location`; delete returns 204; validation uses 400; missing resources 404; conflicts 409. JSON uses ISO-8601 UTC timestamps and decimal string/number values for `BigDecimal` money. Pagination is zero-based with bounded `size`; sort fields are server-controlled.

Products support text search and category filtering. Orders require `Idempotency-Key`, scoped to the authenticated actor. Update endpoint additions require OpenAPI and tests. See [OpenAPI](openapi.yaml), [authentication](authentication.md), and [pagination](pagination.md).


