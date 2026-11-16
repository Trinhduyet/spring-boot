# Reference project review

Reviewed on 2026-11-09 from public repository pages and their README/package listings. The banking repository's source tree was visible. The Books-API source subtree did not load in the available browser, so conclusions about it are limited to its repository page and README; class-level assertions are intentionally avoided.

## Books-API

[Aaqibhafeezkhan/Books-API](https://github.com/Aaqibhafeezkhan/Books-API) describes a Java 17 / Spring Boot 3.2 book-search API that wraps Open Library, supports title/author/subject/ISBN filters and paging, provides OpenAPI UI, and uses an in-process Caffeine cache. Its public repo page shows a single Maven project with `src`, Dockerfile, Procfile, and hosting descriptors. The README highlights endpoint behavior and cache keys, but does not expose controller/service/repository class names in the page that could be inspected here.

**Useful ideas:** a small end-to-end search feature, explicit page/size inputs, external API boundary, cache key including every response-affecting filter, and API documentation.

**Cautions:** local in-memory caching is per process and should not be mistaken for shared cache consistency; external API latency and failure handling need explicit tests; README deployment claims are not verification evidence. Source-level DTO/entity/mapper/security/test design was not independently confirmed.

## BankingPortal-API

[abhi9720/BankingPortal-API](https://github.com/abhi9720/BankingPortal-API) exposes a package-by-layer tree with `config`, `controller`, `dto`, `entity`, `exception`, `mapper`, `repository`, `security`, `service`, `type`, and `util`. The service package has interface/implementation pairs such as `AccountService` / `AccountServiceImpl`, and controllers include account/auth/user endpoints plus a global exception handler. DTOs include request/response types. Its README describes registration, PIN, deposits/withdrawals, transfers, transaction history, global error handling, MySQL configuration, and JWT-based security. Its Maven dependencies include web, JPA, validation, security, JWT, mail, cache, Redis, MapStruct, and Actuator.

**Useful ideas:** separate API DTOs from entities, a centralized MVC exception handler, dedicated security configuration, and services to hold use-case logic.

**Cautions:** the repository has a wide package-per-layer structure and many service interfaces paired with implementations; that adds navigation and indirection when there is only one implementation. Its README TODOs include pagination and token persistence decisions, and a banking domain plus JWT, email, Redis, geolocation, and OTP introduces more concerns than this learning API needs. Keep authentication and service abstractions proportional to the use case.

## Structure selected here

Use package-by-feature with a small set of layers inside each feature:

```text
product/{controller,dto,service,repository,entity}
category/{controller,dto,service,repository,entity}
```

This preserves the BankingPortal separation of HTTP, DTO, persistence, and service responsibilities while reducing cross-feature scattering. Use a service class directly when there is no real alternative implementation; add an interface when a boundary or substitution need exists. Keep the Books API lesson about pagination and cache boundaries, but do not add remote book-provider or distributed-cache infrastructure to the catalog teaching project.

**Pitfalls:** don't copy code because a repository is popular; inspect behavior and tests, and separate README statements from verified source/runtime behavior.

**Lab:** trace the BankingPortal account path from controller to service interface/implementation/repository, then compare with `ProductController` → `ProductService` → `ProductRepository`. Source: [project structure](project-structure.md), [ProductController](../../src/main/java/com/example/catalog/product/api/controller/ProductController.java), [ProductService](../../src/main/java/com/example/catalog/product/application/service/ProductService.java).


