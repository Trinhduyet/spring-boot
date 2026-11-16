# Validation

HTTP input is validated at the boundary with Jakarta Validation; use `@Valid` on request bodies, positive IDs, bounded collection sizes, safe idempotency-key syntax and explicit pagination bounds. Domain/application services must still enforce invariants that can be bypassed outside HTTP: nonzero stock adjustments, active product state, reserved-stock limits and legal order transitions.

Database constraints protect uniqueness and nonnegative quantities under races. Never assume validation annotations replace a unique or check constraint. Add tests for invalid and boundary values.


