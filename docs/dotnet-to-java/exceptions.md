# Exceptions and errors

Java has checked and unchecked exceptions; Spring application/domain failures are usually unchecked. Checked exceptions must be declared/caught and often make little sense for business conflicts. Catch at a boundary where recovery or translation is meaningful. `ApiExceptionHandler` maps known cases to HTTP status and Problem Details; an unexpected error should remain generic and be logged with correlation ID.

Never return stack traces, SQL messages, or token parser errors. Avoid broad `catch (Exception)` that converts every failure to 400.


