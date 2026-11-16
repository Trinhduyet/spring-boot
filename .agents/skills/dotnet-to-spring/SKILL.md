---
name: dotnet-to-spring
description: Use when helping a senior .NET developer translate C#, ASP.NET Core, EF Core, DI, async, security, or testing patterns to Java and Spring Boot.
---

# .NET to Spring workflow

## Trigger
Use for migration questions, code translation, or onboarding documentation.

## Workflow
1. Identify the .NET behavior and runtime assumptions, not only syntax.
2. Map to Java 21 and Spring idioms with explicit lifecycle, transaction, threading, and security semantics.
3. Provide paired examples, pitfalls, and a repository-appropriate implementation.
4. Update the migration guide when durable learning is requested.

## Checklist and acceptance
- [ ] Examples compile conceptually against Java 21/Spring Boot 3.
- [ ] Explain ownership, nullability, exceptions, transactions, and async differences.
- [ ] Avoid direct mechanical translation of framework idioms.

## Example
Compare scoped ASP.NET services with Spring singleton beans plus request-scoped alternatives; explain that singleton is the default and mutable request state is unsafe.
