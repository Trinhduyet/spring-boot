# Java 21 for C# developers

## Classes, interfaces, records, enums and sealed types

Java classes are nominal reference types. A class has one superclass and can implement many interfaces. Use `final` when inheritance is not an extension point. Records are shallowly immutable data carriers: their components are final, but referenced objects may still be mutable. Sealed types make a hierarchy explicit.

```java
public sealed interface Payment permits CardPayment, CashPayment { }
public record CardPayment(String token) implements Payment { }
public record CashPayment(java.math.BigDecimal amount) implements Payment { }
public enum OrderState { NEW, PAID, CANCELLED }
```

```csharp
public abstract record Payment;
public sealed record CardPayment(string Token) : Payment;
public sealed record CashPayment(decimal Amount) : Payment;
```

Java interfaces can have `default` and `static` methods, but have no instance state. Constructors do not inherit. `private`, package-private (no modifier), `protected`, and `public` are access levels; package-private is commonly useful for implementation details. `static final` is a constant reference, not deep immutability.

An abstract class can hold instance state and provide partial implementation, but Java allows extending only one class. Interfaces let one type implement several contracts. A Java reference can hold an implementation subtype, so overridden methods dispatch polymorphically just as C# virtual/interface calls do.

```java
abstract class PaymentMethod { abstract BigDecimal charge(BigDecimal amount); }
interface Refundable { void refund(BigDecimal amount); }
final class CardPayment extends PaymentMethod implements Refundable {
    @Override BigDecimal charge(BigDecimal amount) { return amount; }
    @Override public void refund(BigDecimal amount) { }
}
```

**Pitfalls:** records are not a replacement for JPA entities; JPA needs entity identity and a no-argument constructor. Prefer composition over inheriting framework types. Use `BigDecimal`, not `double`, for money.

**Lab:** add a sealed `Discount` policy with percentage and fixed amount records; write exhaustive `switch` logic and tests. Source: [ProductSnapshot](../../src/main/java/com/example/catalog/product/application/result/ProductSnapshot.java), [OrderStatus](../../src/main/java/com/example/catalog/ordering/domain/model/OrderStatus.java).

## Collections and functional Java

`List`, `Set`, and `Map` are interfaces; `ArrayList`, `HashSet`, and `HashMap` are common implementations. `List.of(...)` and `Map.of(...)` create unmodifiable collections (mutating them throws). Arrays have fixed length and can hold primitives; generic collections cannot use primitive type arguments and use wrappers such as `Integer`.

```java
var names = products.stream()
        .filter(ProductResponse::active)
        .map(ProductResponse::name)
        .distinct()
        .toList();
var bySku = products.stream().collect(java.util.stream.Collectors.toMap(
        ProductResponse::sku, java.util.function.Function.identity()));
```

C# equivalent: `products.Where(p => p.Active).Select(p => p.Name).Distinct().ToList()`. Java Streams are lazy until a terminal operation, are usually single-use, and execute in memory. EF Core `IQueryable` builds a database expression; a Java Stream over entities does not. Use a repository query/JPQL to filter in SQL. `flatMap` maps one item to zero or more items and flattens the streams. `Optional<T>` models a possibly absent return value; avoid it for entity fields, request DTOs, or method parameters.

**Pitfalls:** do not mutate shared state inside a stream; `toList()` returns an unmodifiable list; `Collectors.toMap` throws on duplicate keys unless a merge function is supplied.

**Lab:** group product names by first letter, count each group, and compare the in-memory result with a paged repository search. Source: [ProductRepository](../../src/main/java/com/example/catalog/product/infrastructure/persistence/repository/ProductRepository.java).

## Exceptions and generics

Checked exceptions must be caught or declared (`IOException`); unchecked exceptions extend `RuntimeException` and are common for programming errors and rejected application operations. Try-with-resources closes `AutoCloseable` values even on failure.

```java
try (var reader = java.nio.file.Files.newBufferedReader(path)) {
    return reader.readLine();
} catch (java.io.IOException ex) {
    throw new IllegalStateException("Could not read input", ex);
}
```

C# generally has no checked exception enforcement and uses `using`/`await using` for disposal. Java generics use type erasure: most type arguments are unavailable at runtime and primitives need wrappers. `? extends T` is a producer bound; `? super T` is a consumer bound (PECS). Unlike C#, Java generic types are invariant by default.

```java
static <T> T first(java.util.List<? extends T> values) { return values.getFirst(); }
static void addNames(java.util.List<? super String> target) { target.add("Ada"); }
```

**Pitfalls:** don't catch and ignore; preserve the cause when translating exceptions. Avoid raw types and unchecked casts. Do not test a generic type argument with `instanceof List<String>` because it is erased.

**Lab:** write a generic page mapper and test empty, one-item, and exception cases. Source: [ApiExceptionHandler](../../src/main/java/com/example/catalog/api/error/ApiExceptionHandler.java).

## Concurrency and JVM essentials

Use `ExecutorService` for bounded worker pools and `CompletableFuture` for explicit asynchronous composition. Virtual threads (Java 21) make blocking tasks cheaper to schedule, but do not make shared state safe or increase database connection capacity. Prefer immutable values and synchronization/atomic types for shared state. ASP.NET `Task` is commonly paired with async I/O; Java server code often uses blocking calls on managed threads, while WebFlux is a separate reactive model.

```java
try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
    var result = executor.submit(() -> loadFromBlockingClient());
    return result.get();
}
```

The JDK includes tools and libraries; the JVM executes bytecode. Each thread has a stack; objects live in the managed heap; garbage collection reclaims unreachable objects. The Java Memory Model defines visibility and ordering between threads. JIT compilation can optimize hot code after startup.

**Pitfalls:** avoid unbounded executor queues, blocking while holding locks, and assuming `volatile` makes compound operations atomic. Diagnose with thread dumps, heap dumps, logs, and Java Flight Recorder before tuning GC.

**Lab:** measure a bounded executor and virtual-thread executor against an artificial blocking task; record throughput and explain why database-bound concurrency still needs a limit. Source: [InventoryService](../../src/main/java/com/example/catalog/inventory/application/service/InventoryService.java).


