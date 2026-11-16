# JVM, concurrency, and debugging basics

The JDK includes compiler and tools; the JVM loads and executes bytecode. “JRE” is the traditional runtime distribution concept (JVM plus runtime libraries), while current Java distributions commonly ship a JDK or custom runtime image. Each thread has a stack; objects live in a garbage-collected heap. JIT compilation optimizes hot code. The Java Memory Model defines visibility and ordering across threads; immutable data and synchronization make those rules easier to reason about.

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    var answer = executor.submit(() -> blockingLookup());
    return answer.get();
}
```

Compared with ASP.NET Core, Java's virtual threads (available in Java 21) make blocking tasks cheaper to schedule, while `CompletableFuture` models explicit asynchronous composition. They do not increase database connection capacity or make shared mutable data thread-safe. `ExecutorService` provides bounded worker pools; choose based on workload and resource limits.

**Pitfalls:** avoid unbounded queues, blocking while holding locks, and assuming `volatile` makes read-modify-write atomic. Diagnose before tuning with logs, thread/heap dumps, and Java Flight Recorder.

**Lab:** compare a small bounded executor with virtual threads on a blocking workload; explain why database pool size remains a separate limit. Source: [InventoryService](../../src/main/java/com/example/catalog/inventory/application/service/InventoryService.java).


