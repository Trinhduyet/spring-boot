# Debugging Spring Boot and JVM

- **JVM memory/GC:** collect heap/GC metrics and a heap dump under controlled access; distinguish heap exhaustion from native memory/thread exhaustion. A larger heap can increase GC pauses.
- **Spring bean lifecycle:** inspect condition evaluation (`--debug`) and bean startup logs. Constructor side effects run before the app is ready; avoid remote calls from constructors.
- **Spring AOP:** set breakpoints on proxy entry and target; transactional self-invocation is not intercepted. Inspect actual proxy and transaction boundaries.
- **Hibernate dirty checking:** inspect SQL and entity state; flush can happen before queries and at commit. A read-only transaction is an optimization hint, not a substitute for immutability.
- **N+1:** enable Hibernate statistics or datasource SQL instrumentation in test; assert bounded query counts with realistic fixtures.
- **Type erasure:** generic type arguments are mostly absent at runtime; serializers/frameworks use signatures/metadata and may need explicit type tokens.
- **Thread dumps/JFR:** use `jcmd <pid> Thread.print`, `jcmd <pid> JFR.start`, and inspect blocked threads, lock waits, pool starvation and hot allocations. Protect dumps as sensitive data.
- **HikariCP:** compare active/idle/pending connections and timeout metrics with DB limits and total replica count.
- **Transactions:** use the H2 rollback test for basic behavior; it does not establish SQL Server locking/isolation behavior. Do not assume nested service calls create nested transactions; default propagation joins the current transaction.
- **Virtual threads:** check JVM support, framework configuration and connection pool sizing; virtual threads do not make a saturated DB faster.

Use Actuator and correlation IDs first; capture dumps only through a restricted operational workflow.


