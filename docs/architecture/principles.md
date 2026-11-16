# Architecture principles

1. Feature ownership first; layer within the feature.
2. Keep framework and persistence concerns at module edges when domain complexity justifies the boundary.
3. Enforce correctness with database constraints as well as application validation.
4. Make money and time precise (`BigDecimal`, UTC `Instant`).
5. Every mutation has an explicit transaction and concurrency strategy.
6. Keep the example security boundary small: local/test are isolated, dev uses authenticated HTTP Basic requests.
7. Prefer observable, bounded operations and predictable error contracts.
8. Add abstraction only at a meaningful boundary.

Examples: SKU uniqueness is a normalized business rule plus a unique index; inventory reservation is atomic and transactional; API entities are mapped to records.



Responsibility subpackages and the current request flow are documented in [project structure](project-structure.md) and [ADR 0005](../adr/0005-responsibility-packages.md). Application code must not depend on HTTP DTOs; input/result/port values remain framework-free.
