# .NET to Spring Boot transition

## Purpose

Map familiar ASP.NET Core and EF Core concepts to this product's Java implementation. Java uses nominal classes, checked exceptions, erased generics and a different thread/transaction model; learn the semantics rather than doing text substitution.

Start with `CatalogApplication`, then inspect `ProductController`, `ProductService`, `ProductRepository`, `SecurityConfig`, and `InventoryService`. The local profile connects to an existing SQL Server and permits local calls; the dev profile demonstrates HTTP Basic authentication. Flyway is disabled by default until schema review and explicit opt-in.

Recommended order: language/runtime differences, Spring IoC/AOP, MVC/validation, JPA lifecycle and transactions, security, tests, then production operations. Follow the [14-day plan](roadmap-14-days.md).


