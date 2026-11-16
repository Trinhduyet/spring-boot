# Naming conventions

Packages and variables use lower camel case; Java types use UpperCamelCase; constants use `UPPER_SNAKE_CASE`. Requests are named by intent (`CreateOrderRequest`), API views with `Response`, entities with `Entity`, repositories with `Repository`, and application services with an action or feature name. Flyway files use `V<number>__<imperative_description>.sql`.

Prefer explicit types and precise units (`availableQuantity`, `createdAt`) over `data`, `value`, or abbreviations. For public API additions, retain stable, versioned `/api/v1` routes and document every field.


