# Asynchronous programming

Spring MVC is synchronous at the controller boundary by default. Java virtual threads can reduce the cost of blocked request threads when explicitly enabled and measured; they do not increase SQL Server connections, make blocking DB calls nonblocking, or provide a durable job queue. `CompletableFuture` composes async work but requires an explicit executor and context propagation strategy.

Use a broker for durable inter-service jobs and an outbox for events coupled to DB commits. `@Scheduled` is suitable for simple local schedules, not distributed exactly-once processing. Bound concurrency and propagate deadlines/cancellation.


