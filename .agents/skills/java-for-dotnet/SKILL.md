---
name: java-for-dotnet
description: Translate Java 21 and Spring concepts for experienced C# and ASP.NET Core developers.
---

# Java for .NET developers

## Trigger
Use for Java/Spring onboarding, C# comparisons, or code changes needing framework behavior explained.

## Workflow
1. Identify .NET behavior and runtime assumptions.
2. Show idiomatic Java 21 and Spring, not mechanical syntax translation.
3. Explain lifecycle, nullability, exceptions, transactions, and threading as relevant.
4. Link examples to project source and include a practice task.

```java
public record ProductView(long id, String sku) { }
```
```csharp
public sealed record ProductView(long Id, string Sku);
```

## Checklist
- [ ] Explain behavior and important differences.
- [ ] Include Java, .NET equivalent, pitfall, lab, and source links.
- [ ] Distinguish in-memory streams from database queries.

## Check
Review links and snippets; run `./mvnw clean test` when source changes.
