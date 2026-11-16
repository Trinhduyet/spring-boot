# Ten C# and Java examples

Each pair shows the shape, not a line-for-line conversion. Production behavior depends on database, transaction, security and lifecycle boundaries.

## 1. ASP.NET endpoint / Spring MVC

```csharp
[HttpGet("{id:long}")]
public async Task<ActionResult<ProductDto>> Get(long id) =>
    await service.Get(id) is { } item ? Ok(item) : NotFound();
```
```java
@GetMapping("/{id}")
public ProductResponse get(@PathVariable long id) { return service.get(id); }
```
Similar: thin HTTP adapter. Different: Java method shown is synchronous and service throws a typed not-found error. Pitfall: do not put JPA entities in either API. Production: document status/error contract and test it.

## 2. DI construction

```csharp
services.AddScoped<IProductService, ProductService>();
```
```java
@Service
class ProductService {
  private final ProductRepository repository;
  ProductService(ProductRepository repository) { this.repository = repository; }
}
```
Similar: dependencies supplied by container. Different: Spring service is singleton by default; transaction is proxy advice, not a request scope. Pitfall: never store request data on singleton fields. Production: immutable fields and constructor injection.

## 3. Request validation

```csharp
public sealed record CreateProduct([Required] string Sku,
  [Range(typeof(decimal), "0", "999999999")] decimal Price);
```
```java
public record CreateProductRequest(@NotBlank @Size(max=64) String sku,
  @NotNull @DecimalMin("0.00") @Digits(integer=12, fraction=2) BigDecimal price) {}
```
Similar: declarative boundary validation. Different: Java uses `BigDecimal`; validation triggers through `@Valid`. Pitfall: annotations don't protect DB races. Production: add constraints and service-level invariants.

## 4. EF query / Spring Data query

```csharp
var page = await db.Products.Where(p => p.Name.Contains(q)).ToListAsync(ct);
```
```java
Page<ProductEntity> page = repository.search(q, pageable);
```
Similar: query in database. Different: Java Stream `.filter()` after repository retrieval runs in memory. Pitfall: accidentally materialize all rows. Production: use bounded `Pageable`, indexes and query plan checks.

## 5. Update managed entity

```csharp
var product = await db.Products.FindAsync(id, ct);
product.Name = input.Name;
await db.SaveChangesAsync(ct);
```
```java
@Transactional
ProductResponse update(long id, UpdateProductRequest input) {
  ProductEntity product = repository.findById(id).orElseThrow(...);
  product.update(input.name(), input.price(), input.active());
  return ProductResponse.from(product); // dirty checking flushes at commit
}
```
Similar: unit-of-work tracks changes. Different: JPA managed mutation can persist without explicit `save`. Pitfall: modifying a detached entity does not persist. Production: bound the transaction and use `@Version`.

## 6. Transaction boundary

```csharp
await using var tx = await db.Database.BeginTransactionAsync(ct);
await ReserveStockAsync(order, ct);
await db.SaveChangesAsync(ct);
await tx.CommitAsync(ct);
```
```java
@Transactional
OrderResponse create(...) {
  // save order + reserve inventory + save lines + audit
}
```
Similar: atomic DB work. Different: Spring default propagation joins nested service calls; AOP self-invocation bypasses proxy. Pitfall: don't call a `@Transactional` method on `this`. Production: put one use case boundary on a public service method and test rollback in SQL Server.

## 7. HTTP Basic security

```csharp
app.UseAuthentication();
app.UseAuthorization();
```
```java
http.authorizeHttpRequests(a -> a.requestMatchers("/api/**").hasRole("USER"))
  .httpBasic(Customizer.withDefaults());
```
Similar: authentication populates a principal, then route authorization checks it. Different: this example uses HTTP Basic with an in-memory BCrypt user rather than bearer tokens. Pitfall: credentials require TLS outside loopback. Production identity management is outside this learning sample.

## 8. External service call

```csharp
var client = factory.CreateClient("catalog-partner");
using var response = await client.GetAsync(uri, ct);
response.EnsureSuccessStatusCode();
```
```java
PartnerResponse result = restClient.get().uri(uri).retrieve().body(PartnerResponse.class);
```
Similar: configured reusable client. Different: blocking MVC call occupies a thread; `WebClient` has reactive semantics. Pitfall: default infinite timeouts/retries. Production: set connect/read deadlines, bounded retries for safe calls, metrics and circuit-breaker policy.

## 9. Parallel calls

```csharp
var results = await Task.WhenAll(ids.Select(id => client.Get(id, ct)));
```
```java
var results = CompletableFuture.allOf(calls.toArray(CompletableFuture[]::new))
  .thenApply(ignored -> calls.stream().map(CompletableFuture::join).toList());
```
Similar: compose concurrent work. Different: executor/context propagation must be explicit and errors surface differently. Pitfall: unbounded parallel DB calls. Production: bound concurrency, timeout, cancel, and propagate tracing/security context.

## 10. Integration test

```csharp
await using var app = new WebApplicationFactory<Program>();
var response = await app.CreateClient().GetAsync("/api/products");
```
```java
mockMvc.perform(get("/api/v1/products").with(httpBasic("learner", "test-only-password")))
  .andExpect(status().isOk());
```
Similar: exercise HTTP contract. Different: MockMvc uses mock servlet processing and not a network socket. Pitfall: H2 does not prove SQL Server locks. Further SQL Server-specific verification requires an authorized SQL Server environment.

### Senior-level runtime pitfalls

JVM heap and native memory are distinct; GC tuning follows allocation and pause evidence. Generics erase at runtime. Hibernate dirty checking, lazy loading and flush timing differ from EF Core. Hikari connection count is a global database budget across replicas. Virtual threads can help blocking request scalability, but do not add DB capacity. Use JFR and thread dumps to locate allocation, CPU, lock waits and thread starvation.



