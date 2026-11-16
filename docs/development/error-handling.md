# Error handling

Expected business failures use feature-specific unchecked exceptions and are mapped centrally by `ApiExceptionHandler` to RFC 9457 `ProblemDetail`. Validation failures return 400, missing resources 404, uniqueness/state conflicts 409, and unknown failures remain generic 500 responses handled by Spring without exposing internals.

Do not catch and suppress exceptions. Catch persistence errors only where their meaning is known; database constraints remain the final integrity check. Include stable error codes as contracts evolve. Never return exception class names, SQL, tokens or stack traces to clients.


