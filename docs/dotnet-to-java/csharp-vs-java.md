# C# and Java reference

| .NET / C# | Java / Spring equivalent | Important difference |
|---|---|---|
| `Program.cs` top-level statements | `@SpringBootApplication` main | Spring builds an application context |
| `namespace` | `package` | Package is a compile-time namespace and directory convention |
| `using` | `import` | Java has no C# global using feature by default |
| `record` | Java `record` | Java records are final, shallowly immutable data carriers |
| `class` | `class` | Java classes are single-inheritance, multiple-interface |
| `interface` | `interface` | Default/static methods exist; use for real boundaries |
| `sealed` class | `final` class | Java `sealed` also supports `permits` hierarchies |
| `init` property | record component / constructor | No general `init`-only property syntax |
| Nullable reference types | `Optional<T>` at boundaries + annotations | Java nullability is not enforced by compiler |
| `var` local | `var` local | Java local `var` is inferred but not dynamic |
| LINQ `Select` | Stream `map` | Streams are in-memory, usually lazy and single-use |
| LINQ `Where` on IQueryable | JPQL/specification predicates | Query executes in database; avoid loading then filtering |
| `async` / `await` | synchronous MVC, `CompletableFuture`, virtual threads | No language-level async state machine equivalent |
| `Task<T>` | `CompletionStage<T>` / executor result | Blocking servlet calls remain blocking unless moved |
| `CancellationToken` | thread interruption / request timeout | Cancellation propagation must be designed |
| `IDisposable` / `using` | `AutoCloseable` / try-with-resources | Deterministic resource cleanup |
| GC / CLR | JVM GC | Choose collector from measured workload, not analogy |
| NuGet | Maven Central / Maven | Dependency graph and lifecycle are build-tool-managed |
| `.csproj` | `pom.xml` | Maven lifecycle and dependency management via BOM |
| `dotnet restore` | `./mvnw dependency:resolve` | Wrapper pins Maven distribution |
| `dotnet build` | `./mvnw compile` | Maven phases execute in ordered lifecycle |
| `dotnet test` | `./mvnw test` | Surefire runs unit tests; Failsafe conventionally runs IT tests |
| `dotnet publish` | `./mvnw package` / Boot repackage | Produces executable Spring Boot JAR |
| `appsettings.json` | `application.yml` | Property sources/profile/ENV precedence differ |
| `IConfiguration` | `Environment` | Bind structured settings using `@ConfigurationProperties` |
| `IOptions<T>` | `@ConfigurationProperties` | Supports validation and immutable config binding |
| `IServiceCollection` | Spring `ApplicationContext` | Component scanning and auto-configuration add beans |
| `AddSingleton` | singleton-scoped Spring bean | Singleton is per application context, not process-global |
| `AddScoped` | request scope (rough analogy) | Typical Spring services are singleton; transactions are not scopes |
| `AddTransient` | prototype bean | Prototype lifecycle cleanup differs |
| Constructor injection | Constructor injection | Same testability and immutability benefits |
| `ControllerBase` | `@RestController` | MVC annotations and converters build the HTTP adapter |
| `IActionResult` | `ResponseEntity<T>` | `ProblemDetail` is built in for problem responses |
| Middleware | Servlet `Filter` / Spring Security filter | Filter ordering/security chain matters |
| MVC action filter | Spring MVC `HandlerInterceptor` | Interceptor runs after servlet filters and handler mapping |
| DataAnnotations | Jakarta Validation | `@Valid` triggers validation at boundaries |
| `ModelState` | `BindingResult` / exception advice | Error response is application-owned contract |
| EF Core `DbContext` | `EntityManager` / Persistence Context | Hibernate dirty checking tracks managed entities |
| `DbSet<T>` | Spring Data `JpaRepository<T, ID>` | Repository methods generate queries by naming rules |
| EF Core migration | Flyway SQL migration | Flyway migration is externalized SQL and versioned history |
| LINQ-to-SQL | JPQL / Criteria / Querydsl | Streams run after rows enter memory |
| `SaveChanges` | transaction flush/commit | JPA may flush before commit or before a query |
| Change tracker | Persistence Context | Managed entity updates can persist without explicit save |
| `AsNoTracking` | DTO projection / read-only query | No direct universal equivalent; query design matters |
| Include navigation | fetch join / entity graph | Collection pagination can duplicate or break result counts |
| `rowversion` concurrency | `@Version` | Optimistic-lock exceptions occur at flush/commit |
| EF Core execution strategy | Spring Retry / DB transaction policy | Retries must not duplicate external side effects |
| SQL Server | SQL Server JDBC driver / Hibernate SQL Server dialect | Existing database semantics still need explicit mapping |
| `ILogger<T>` | SLF4J `Logger` | Parameterized logging and MDC are common |
| Serilog JSON sink | Spring structured logging / Logback | Configure PII/token redaction explicitly |
| `Activity` / OpenTelemetry | Micrometer Tracing / OpenTelemetry | Context propagation depends on executor/reactive model |
| Health Checks | Actuator health groups | Do not expose sensitive details publicly |
| ASP.NET Core authentication handler | Spring `SecurityFilterChain` | Both install authentication in a request pipeline/filter chain |
| `[Authorize]` | `authorizeHttpRequests` | Spring matchers select routes and required authority |
| `ClaimsPrincipal` | `Principal` / `Authentication` | Pass actor context deliberately into a use case |
| CORS middleware | Spring CORS config | Configure only when a browser client needs cross-origin access |
| xUnit | JUnit 5 | Lifecycle, extension and parameterized tests differ |
| Moq | Mockito | Mockito strictness and final-class support vary by setup |
| WebApplicationFactory | `@SpringBootTest` / `MockMvc` | Context startup is heavier; use slices where possible |
| TestServer | MockMvc / embedded server | MockMvc does not exercise a real network stack |
| EF test database | H2 in-memory test profile | Fast feedback; does not establish SQL Server compatibility |
| Polly | Resilience4j / Spring Cloud Circuit Breaker | Retry only safe/idempotent operations |
| `BackgroundService` | `@Scheduled` / `ApplicationRunner` / worker | Thread lifecycle and shutdown are explicit concerns |
| Hosted service DI scope | prototype/request scoped worker | Create scopes deliberately for per-job transactions |
| `Channel<T>` | `BlockingQueue`, Reactor, messaging broker | Pick based on backpressure and durability needs |
| `lock` / `Monitor` | `synchronized`, `Lock` | Database concurrency needs database locks/constraints |
| `ConcurrentDictionary` | `ConcurrentHashMap` | Never use a local map for distributed coordination |
| `SemaphoreSlim` | `Semaphore` / `SemaphoreSlim` not standard | Virtual threads reduce blocking cost, not DB capacity |
| `Task.WhenAll` | `CompletableFuture.allOf` | Exceptions and cancellation composition differ |
| `Guid` | `UUID` | SQL Server `uniqueidentifier` maps to `UUID` |
| `DateTimeOffset` | `Instant` / `OffsetDateTime` | Store instants; use zone IDs for calendar rules |
| `decimal` | `BigDecimal` | Never construct monetary `BigDecimal` from binary double |
| `decimal?` | `BigDecimal` nullable / `Optional` | Prefer Optional mainly for return values |
| `switch` expression | Java switch expression | Pattern matching support depends on Java version |
| NuGet private feed | Nexus / Artifactory | Configure credentials outside source control |
| GitLab CI templates | GitLab CI `include` | Pin reviewed templates and keep secrets in protected variables |
| Docker Compose | Docker Compose YAML | Validate per environment and provision secrets outside source |
| GitLab deploy job | SSH to Docker Host | Restrict production to protected release tags and manual approval |
| Kestrel | Embedded Tomcat/Jetty/Undertow | Servlet thread pools and shutdown settings matter |
| `HttpClientFactory` | `RestClient` / `WebClient` | Reuse configured clients; set timeouts and TLS |
| `IHostedLifecycleService` | Spring lifecycle callbacks | Avoid doing heavy network work in bean constructors |
| `IAsyncEnumerable<T>` | `Stream<T>` / `Flux<T>` | Java Stream is not asynchronous or backpressured network streaming |
| Source generators | annotation processors | Generated code appears during compile; debug processors when needed |
| Reflection emit | reflection / bytecode libraries | Runtime codegen is less routine and has module constraints |
| Native AOT | GraalVM Native Image | Reflection/proxy metadata needs configuration |




