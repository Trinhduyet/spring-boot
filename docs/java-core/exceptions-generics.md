# Exceptions and generics

Java checked exceptions (for example `IOException`) must be caught or declared; unchecked exceptions derive from `RuntimeException`. C# does not enforce checked exceptions. Use try-with-resources for `AutoCloseable`, similar to C# `using`.

```java
try (var input = Files.newBufferedReader(path)) {
    return input.readLine();
} catch (IOException ex) {
    throw new IllegalStateException("Read failed", ex);
}
```

Java generics are invariant by default and mostly erased at runtime; C# retains generic runtime metadata and supports variance annotations. Primitive types cannot be generic arguments, so use `Integer`, `Long`, etc. Use `? extends T` when reading producers and `? super T` when writing consumers (PECS).

```java
static <T> T first(List<? extends T> values) { return values.getFirst(); }
static void addLabels(List<? super String> labels) { labels.add("primary"); }
```

**Pitfalls:** preserve exception causes, do not catch and ignore, avoid raw types and unchecked casts, and do not use `instanceof List<String>` because parameterized types are erased.

**Lab:** write a generic mapper for a page of values and test empty and populated cases. Source: [API exception handler](../../src/main/java/com/example/catalog/api/error/ApiExceptionHandler.java).


