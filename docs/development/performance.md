# Performance

Bound page size (maximum 100), avoid entity-to-JSON serialization, and inspect generated SQL before adding fetch joins. `open-in-view` is disabled. Search currently uses contains matching and will not use a normal B-tree index efficiently for large datasets; consider SQL Server full-text indexing after measuring. Hikari pool size must be sized against database capacity and total application hosts, not copied from defaults.

Use Actuator metrics and traces to distinguish slow SQL, lock waits and application CPU. Load test with production-like data before changing pool sizes or adding Redis.


