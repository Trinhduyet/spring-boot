# Đọc và hiểu dự án Java Spring Boot

Tài liệu này dành cho người đã làm .NET và đang đọc dự án lần đầu. Sau khi học, bạn có thể lần theo một HTTP request từ controller đến SQL Server, giải thích annotation nào được ai xử lý và đặt breakpoint đúng chỗ. Các ví dụ dựa trên source hiện tại: Java 21, Spring Boot 3.5, Spring MVC, Spring Data JPA và SQL Server.

## 1. Bắt đầu từ đâu?

1. Đọc cây thư mục bên dưới và mở [CatalogApplication](../src/main/java/com/example/catalog/CatalogApplication.java).
2. Đọc Java core ở mục 3, sau đó tìm các kiểu tương ứng trong [ProductService](../src/main/java/com/example/catalog/product/application/service/ProductService.java).
3. Đọc luồng `POST /api/v1/products` ở mục 5 và đặt breakpoint theo mục 9.
4. Tra bảng annotation ở mục 6 khi gặp `@...` trong code.
5. Thực hành các bài lab ở mục 10, rồi đọc tiếp tài liệu chuyên sâu ở cuối trang.

Để xem HTTP contract khi ứng dụng local đang chạy, mở [Swagger UI](http://localhost:8080/swagger-ui/index.html). Compose hiện bind cổng 8080 vào `127.0.0.1` của máy chạy Docker.

## 2. Cấu trúc dự án

```text
spring-boot-production-starter/
├── pom.xml                        # dependency và cấu hình Maven
├── mvnw, mvnw.cmd                 # Maven Wrapper
├── Dockerfile, docker-compose.yml # build/run container local
├── src/main/java/com/example/catalog/
│   ├── CatalogApplication.java    # điểm bắt đầu Spring Boot
│   ├── api/error/                 # xử lý lỗi HTTP dùng chung
│   ├── product/                   # ví dụ chính: quản lý sản phẩm
│   │   ├── api/                   # controller, request, response, mapper
│   │   ├── application/           # service, input, result, mapper, port
│   │   ├── domain/exception/      # lỗi nghiệp vụ của Product
│   │   └── infrastructure/persistence/
│   │       ├── entity/            # JPA mapping
│   │       ├── repository/        # Spring Data queries
│   │       └── adapter/           # triển khai contract khi cần
│   ├── category/, inventory/, ordering/, audit/
│   └── security/                  # security configuration và filter
├── src/main/resources/            # application*.yml, Flyway migration
├── src/test/java/                 # unit, HTTP, H2 và architecture tests
└── docs/                          # tài liệu theo chủ đề
```

Mỗi feature có những thư mục thực sự cần, không bắt buộc tạo đủ mọi thư mục. Xem [cây package đầy đủ](architecture/project-structure.md).

| Thành phần | Nhiệm vụ | Gần với .NET |
|---|---|---|
| `api/controller` | Nhận HTTP, gọi service, chọn status/headers | ASP.NET Core controller |
| `api/request`, `api/response` | Hợp đồng JSON, validation ở đầu vào | Request/response DTO |
| `api/mapper` | Đổi request thành input và result thành response | Mapping trong API layer |
| `application/service` | Quy tắc use case và transaction | Application service |
| `application/input`, `result` | Dữ liệu service nhận và trả | Data record của application |
| `application/port` | Contract được feature khác sử dụng khi thực sự cần | Interface tại ranh giới module |
| `domain/exception` | Lỗi nghiệp vụ | Domain exception |
| `infrastructure/persistence` | Entity, repository, adapter và database query | EF entity, DbContext/repository |

Ví dụ `CategoryInput` chỉ là một Java `record` chứa `name`, `slug`, `active`; tên Input giúp phân biệt dữ liệu service với HTTP request. Nó không phải một thư viện hay pattern bắt buộc của Spring. Ở code hiện tại service vẫn dùng entity/repository của chính feature; xem [ADR 0005](adr/0005-responsibility-packages.md) để hiểu giới hạn này.

## 3. Java core cần biết khi đọc code

### 3.1. JDK, JVM, package và Maven

JDK chứa compiler và công cụ phát triển. `javac` biên dịch `.java` thành bytecode `.class`; JVM chạy bytecode. Maven đọc `pom.xml`, tải dependencies, biên dịch, chạy tests và đóng gói JAR. `mvnw.cmd`/`mvnw` chọn Maven theo cấu hình wrapper của dự án.

`package com.example.catalog.product...;` đặt class vào namespace; `import` cho phép dùng tên ngắn. Package giống namespace trong C#, nhưng package Java **không tự tạo assembly**. Toàn bộ các feature này vẫn trong một Maven module/JAR. Class `public` cấp cao nhất có tên trùng file, ví dụ `ProductService.java` chứa `public class ProductService`.

### 3.2. Class, interface, record và constructor

```java
// Rút gọn từ ProductService: constructor injection
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
}
```

`private` giới hạn quyền truy cập; `final` ở đây nghĩa là reference `repository` không được gán lại sau constructor, không có nghĩa object bên trong hoàn toàn immutable. Spring tạo service và cung cấp repository qua constructor. `interface ProductRepository extends JpaRepository<ProductEntity, Long>` khai báo contract; Spring Data tạo implementation lúc runtime.

```java
// Dạng hiện tại của CreateProductInput
public record CreateProductInput(String sku, String name, BigDecimal price, Boolean active) {}
```

Record tự sinh constructor, accessor như `input.sku()`, `equals`, `hashCode`, `toString`. Nó gần với C# record dùng cho data; record component là final nhưng object được tham chiếu vẫn có thể mutable. Trong project, entity là class JPA có lifecycle; request/input/result là record tách khỏi entity.

Cách biểu diễn tương tự trong C# là một `record` và một service nhận dependency qua constructor:

```csharp
public record CreateProductInput(string Sku, string Name, decimal Price, bool? Active);

public class ProductService(IProductRepository repository)
{
    private readonly IProductRepository _repository = repository;
}
```

Ví dụ C# minh họa cùng ý tưởng, không phải code có sẵn trong repo. Java dùng `BigDecimal` cho tiền và accessor `sku()`; C# dùng `decimal` và property `Sku`. Lifetime của service .NET phụ thuộc cách đăng ký DI, còn Spring component mặc định là singleton theo context.

### 3.3. Generics, null, Optional và exception

`Optional<ProductEntity>` là container cho giá trị có thể không tồn tại; `Page<ProductResult>` là một trang kết quả có metadata. Trong [ProductService.find](../src/main/java/com/example/catalog/product/application/service/ProductService.java), `findById(id).orElseThrow(...)` lấy entity hoặc ném lỗi nghiệp vụ. `Optional` không tự bảo vệ mọi giá trị khỏi `null`; Java ở đây không có cơ chế nullable reference type giống C# để kiểm tra toàn bộ project lúc compile.

`Long` và `Boolean` là object wrapper, có thể `null`; `long` và `boolean` là primitive, không thể `null`. `CreateProductRequest.active` dùng `Boolean` cùng `@NotNull` để phát hiện giá trị bị thiếu/null. Ngược lại `CategoryRequest.active` là `boolean`; theo binding mặc định hiện tại, field thiếu sẽ có giá trị `false`, nên không tự phân biệt được “thiếu” và `false`.

Java phân biệt checked exception và unchecked exception (`RuntimeException`); method phải khai báo hoặc xử lý checked exception. Lỗi nghiệp vụ của dự án kế thừa runtime exception và được advice ánh xạ sang HTTP. C# không ép khai báo checked exception theo cách này.

### 3.4. Collection, lambda, method reference và kiểu dữ liệu

Trong `CategoryService.list()`, `findAll().stream().map(CategoryResultMapper::toResult).toList()` lấy danh sách entity, lần lượt map sang result rồi tạo list. `::toResult` là method reference, tương đương `entity -> CategoryResultMapper.toResult(entity)`. Stream thao tác trên dữ liệu Java đã lấy trong trường hợp này; không giả định mọi `stream()` được chuyển thành SQL. `Page.map(...)` ở Product là API của Spring Data, dùng để map nội dung page.

`var` suy luận **kiểu tĩnh** từ biểu thức lúc biên dịch, không giống `dynamic` của C#. Dùng `BigDecimal` cho tiền, ví dụ `new BigDecimal("12.50")`; tránh tạo từ `double` nếu cần giá trị thập phân chính xác. `Instant` biểu diễn thời điểm UTC. Ở dự án này Hibernate được cấu hình dùng `TIMESTAMP` và UTC cho các cột SQL Server `DATETIME2`; bản thân `DATETIME2` không lưu timezone, nên quy ước UTC cần được các hệ thống ghi dữ liệu cùng tuân thủ.

## 4. Spring Boot khởi động và nối các class

1. JVM gọi `CatalogApplication.main()`, rồi `SpringApplication.run(...)` tạo và khởi động application context.
2. `@SpringBootApplication` kết hợp cấu hình Boot, auto-configuration và component scan. Vì class nằm tại `com.example.catalog`, scan bao phủ các package con. Entity/repository cũng được Boot tìm trong phạm vi mặc định phù hợp với package gốc này.
3. Spring đọc profile và properties, quyết định auto-configuration dựa trên dependencies, cấu hình và beans đã có. Chẳng hạn web starter cung cấp thành phần MVC; JPA starter cùng datasource cấu hình persistence. Đây là hành vi có điều kiện, không phải mọi dependency lúc nào cũng tạo đủ mọi bean.
4. Component scan nhận diện `@RestController`, `@Service`, `@Component`, `@Repository`, `@Configuration`. Spring Data tạo bean implementation cho repository interface. Với một constructor, Spring có thể inject dependencies mà không cần `@Autowired` trên constructor.
5. Spring tạo singleton beans mặc định trong mỗi application context. Singleton ở đây là một instance theo bean definition/context, không phải singleton toàn JVM. Không lưu state riêng của từng request trong field mutable của singleton service.
6. Web server nhận HTTP; filters và Spring Security chạy trong servlet filter chain, sau đó `DispatcherServlet` điều phối đến controller phù hợp. Filter [CorrelationIdFilter](../src/main/java/com/example/catalog/security/infrastructure/filter/CorrelationIdFilter.java) thêm correlation ID vào log/response.

So với ASP.NET Core: `ApplicationContext` gần với DI container/host, `@Bean` gần với đăng ký một factory, còn component scanning cho phép tự nhận diện một số class. Thứ tự và lifecycle không hoàn toàn trùng; Spring singleton là mặc định, trong khi ứng dụng ASP.NET thường chủ động chọn singleton/scoped/transient khi đăng ký.

## 5. Một request thực sự chạy như thế nào?

Ví dụ `POST /api/v1/products`:

```mermaid
sequenceDiagram
    participant Client
    participant MVC as Spring MVC + Validation
    participant C as ProductController
    participant M as ProductApiMapper
    participant S as ProductService proxy
    participant R as ProductRepository/JPA
    participant DB as SQL Server
    Client->>MVC: POST JSON
    MVC->>MVC: @RequestBody deserialize; @Valid kiểm tra
    MVC->>C: create(CreateProductRequest)
    C->>M: toInput(request)
    C->>S: create(CreateProductInput)
    S->>R: existsBySkuIgnoreCase / saveAndFlush
    R->>DB: SQL trong transaction
    DB-->>R: kết quả
    R-->>S: ProductEntity
    S-->>C: ProductResult sau khi mapper chuyển đổi
    C->>M: toResponse(result)
    C-->>Client: 201 + Location + JSON
```

1. [ProductController](../src/main/java/com/example/catalog/product/api/controller/ProductController.java) có `@RequestMapping("/api/v1/products")` và method `@PostMapping`. `@RequestBody` dùng HTTP message converter/Jackson đọc JSON thành [CreateProductRequest](../src/main/java/com/example/catalog/product/api/request/CreateProductRequest.java).
2. `@Valid` kích hoạt Bean Validation trên request: `sku` không rỗng, `price` không null/âm, v.v. Với trường hợp này, dữ liệu không hợp lệ gây `MethodArgumentNotValidException` trước khi controller method chạy; [ApiExceptionHandler](../src/main/java/com/example/catalog/api/error/ApiExceptionHandler.java) trả `400 ProblemDetail`.
3. Controller gọi [ProductApiMapper](../src/main/java/com/example/catalog/product/api/mapper/ProductApiMapper.java), tạo `CreateProductInput` rồi gọi service. Việc đổi kiểu này là code Java bình thường, không phải phép thuật của annotation.
4. Lời gọi vào Spring proxy của `ProductService.create` thiết lập transaction theo `@Transactional`. Service chuẩn hóa SKU, kiểm tra trùng, tạo entity và gọi repository. Spring Data/JPA/Hibernate thực hiện database access; `saveAndFlush` yêu cầu flush xuống database trong transaction, **không đồng nghĩa commit**. Transaction thường commit khi method proxy hoàn tất thành công.
5. Service chuyển entity thành `ProductResult`, controller map thành `ProductResponse`, tạo URI từ request hiện tại, trả `201 Created` cùng header `Location`. Jackson serialize response thành JSON. Nếu xảy ra lỗi được advice xử lý, client nhận status và ProblemDetail tương ứng.

Để theo một request đọc dữ liệu, bắt đầu tại `ProductController.get`, qua `ProductService.get` và `ProductRepository.findById`. Nếu không có ID, service ném `ProductNotFoundException` và advice trả 404.

## 6. Annotation: ai đọc và khi nào?

Annotation là metadata gắn vào class, method, field hoặc parameter; tự viết `@...` không tự chạy nghiệp vụ. Compiler hoặc framework/provider có thể đọc metadata tùy annotation. Ví dụ Spring đọc mapping/transaction, Hibernate đọc JPA mapping, Bean Validation provider đọc constraint, còn compiler kiểm tra `@Override`. Xem import để biết annotation đến từ `org.springframework`, `jakarta.persistence`, `jakarta.validation` hay Java.

### Spring container và HTTP

| Annotation | Ý nghĩa trong code | Ai xử lý / khi nào |
|---|---|---|
| `@SpringBootApplication` | Bật cấu hình Boot và scan từ package gốc | Spring Boot lúc khởi động |
| `@Component`, `@Service`, `@Repository` | Đánh dấu component/service/persistence adapter cho scan; `@Repository` còn tham gia cơ chế dịch persistence exceptions khi được áp dụng | Spring container lúc đăng ký bean |
| `@Configuration`, `@Bean` | Class cấu hình và method tạo bean; ví dụ `SecurityFilterChain` | Spring container lúc tạo context |
| `@Profile`, `@Value` | Chọn bean theo profile; lấy giá trị property | Spring lúc cấu hình/tạo bean |
| `@RestController` | Controller trả response body; kết hợp vai trò controller và response body | Spring MVC khi đăng ký và xử lý HTTP |
| `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` | Chọn route và HTTP method | Spring MVC khi chọn handler |
| `@RequestBody`, `@PathVariable`, `@RequestParam`, `@RequestHeader` | Bind body, segment URL, query string hoặc header vào parameter | Spring MVC trước khi gọi method |
| `@Valid` | Kích hoạt validation cho object được bind trong trường hợp controller này | Spring MVC + Bean Validation provider |
| `@RestControllerAdvice`, `@ExceptionHandler` | Chọn method xử lý exception và tạo HTTP response dùng chung | Spring MVC khi xử lý exception |
| `@Order` | Xác định thứ tự tương đối, ví dụ `CorrelationIdFilter` | Spring khi sắp xếp các thành phần được hỗ trợ |
| `@Transactional` | Khai báo ranh giới transaction; ví dụ `ProductService.create` | Spring transaction interceptor khi gọi qua proxy |
| `@Override` | Compiler kiểm tra method thực sự override/implement method cha | Java compiler; không tạo Spring bean hay transaction |

`@Autowired` thường xuất hiện trong tài liệu Spring và trong test của dự án. Ở production code dự án dùng constructor injection; constructor duy nhất được Spring tự chọn. Đây cũng là cách dễ unit test vì có thể truyền mock qua constructor.

### Validation

| Annotation | Ví dụ và tác dụng |
|---|---|
| `@NotNull` | Không được null; dùng với `price`, `active` trong Product request |
| `@NotBlank` | String không null và phải có ký tự khác whitespace |
| `@NotEmpty` | Collection/string không null và không rỗng; `CreateOrderRequest.lines` |
| `@Size`, `@Pattern` | Giới hạn độ dài hoặc kiểm tra regex |
| `@Min`, `@Max`, `@DecimalMin`, `@Digits` | Giới hạn giá trị hoặc số chữ số |

Constraint không tự kiểm tra chỉ vì gọi `new CreateProductRequest(...)`. Trong luồng HTTP ở trên, `@Valid` tại controller là điểm kích hoạt validation. `@Valid` cũng được dùng lồng trên order lines. Xem [request Product](../src/main/java/com/example/catalog/product/api/request/CreateProductRequest.java) và [request Order](../src/main/java/com/example/catalog/ordering/api/request/CreateOrderRequest.java).

### JPA và Spring Data

| Annotation | Ý nghĩa trong dự án |
|---|---|
| `@Entity`, `@Table` | Đánh dấu class JPA và ánh xạ bảng, ví dụ `ProductEntity` → `products` |
| `@Id`, `@GeneratedValue(strategy = IDENTITY)` | Khóa chính và ID do database cấp |
| `@Column` | Metadata ánh xạ tên cột, nullability, length, precision/scale; không thay thế migration đã review |
| `@Version` | Optimistic locking: version được kiểm tra khi update entity, giúp phát hiện cập nhật cạnh tranh |
| `@PrePersist`, `@PreUpdate` | JPA lifecycle callback đặt timestamps ở Product entity khi persist/update |
| `@ManyToOne`, `@JoinColumn`, `@IdClass`, `@Enumerated` | Mapping quan hệ, join column, composite ID và enum ở các entity liên quan |
| `@Query`, `@Param` | Khai báo query và bind parameter; Product repository có JPQL và native SQL |
| `@Lock` | Khai báo lock mode cho query repository; hành vi concurrency phụ thuộc database/transaction |

[ProductRepository](../src/main/java/com/example/catalog/product/infrastructure/persistence/repository/ProductRepository.java) là interface, `JpaRepository` cung cấp các thao tác thông dụng. `existsBySkuIgnoreCase` được Spring Data diễn giải theo tên; `search` dùng `@Query`. Hibernate theo dõi entity trong persistence context; khi update entity đang được quản lý trong transaction, dirty checking có thể phát hiện thay đổi và phát SQL khi flush. So sánh với change tracking của EF Core, nhưng không giả định query và locking giữa hai công nghệ hoàn toàn giống nhau.

### Test annotations

`@Test` đánh dấu test JUnit. `@WebMvcTest(ProductController.class)` tạo MVC test slice; trong [ProductControllerTest](../src/test/java/com/example/catalog/product/api/controller/ProductControllerTest.java), `@MockitoBean` thay service bằng mock và `@Import` thêm security/error handler. `@DataJpaTest` tập trung vào JPA/repository; [ProductRepositoryTest](../src/test/java/com/example/catalog/product/infrastructure/persistence/repository/ProductRepositoryTest.java) dùng profile `test` và H2. `@SpringBootTest` tạo application context rộng hơn. `@ActiveProfiles("test")` chọn test profile.

## 7. Transaction và proxy: điểm dễ hiểu sai

Với Spring proxy mặc định, lời gọi từ bean khác vào method `@Transactional` qua proxy mới kích hoạt transaction advice cho lời gọi đó. Gọi một method khác bằng `this.method()` hoặc gọi trực tiếp bên trong cùng object không đi qua proxy, nên annotation của method bên trong không được intercept riêng.

Ví dụ thực tế: `ProductService.list(search, pageable)` gọi overload `list(search, null, pageable)` trong cùng class. Nếu lời gọi ngoài đi vào overload đầu qua proxy, transaction read-only của overload đầu đã được áp dụng; lời gọi nội bộ không kích hoạt thêm advice riêng cho overload thứ hai. Nếu một method không có transaction tự gọi method `@Transactional` nội bộ, không được dựa vào annotation bên trong để tạo transaction mới.

Mặc định Spring rollback khi exception không được xử lý thoát khỏi method là `RuntimeException` hoặc `Error`; checked exception mặc định không rollback trừ khi cấu hình rule phù hợp. `readOnly = true` là hint/optimization tùy transaction manager và database, không phải cam kết phổ quát rằng database sẽ chặn mọi lệnh ghi. `flush` không phải `commit`. Đọc thêm [transaction guide](development/transactions.md) và [Spring transaction annotations](https://docs.spring.io/spring-framework/reference/6.2/data-access/transaction/declarative/annotations.html).

## 8. Configuration và database

| Nơi | Vai trò trong dự án |
|---|---|
| [application.yml](../src/main/resources/application.yml) | `${DB_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}`, Hibernate `validate`, Flyway mặc định tắt, health endpoint |
| [application-local.yml](../src/main/resources/application-local.yml) | Giá trị local cho datasource; password vẫn lấy từ environment |
| [application-test.yml](../src/main/resources/application-test.yml) | H2 in-memory, Hibernate `create-drop`, Flyway tắt cho test profile |
| [docker-compose.yml](../docker-compose.yml) | Chạy API profile `local`, đọc `.env`, bind cổng local |
| [migration V1/V2](../src/main/resources/db/migration/) | SQL định nghĩa schema được kiểm soát bằng Flyway khi được chủ động bật sau review |

Spring property precedence và profile quyết định giá trị cuối cùng. Compose đưa `.env` vào container; không đưa mật khẩu vào Git hoặc tài liệu. Profile `local`/`test` trong [SecurityConfig](../src/main/java/com/example/catalog/security/infrastructure/config/SecurityConfig.java) permit request để học local; profile khác dùng HTTP Basic theo cấu hình. Test profile dùng H2 riêng; `create-drop` của test không phải cấu hình cho SQL Server hiện có. Không bật schema create/update hoặc Flyway trên database có sẵn khi chưa kiểm tra schema, history và review migration.

## 9. Debug từng bước

Mở project bằng IDE hỗ trợ Java/Spring. Nếu ứng dụng chạy **bên trong Docker**, breakpoint trong IDE chỉ bắt được khi có cấu hình remote debug và debugger attach vào JVM container; cấu hình Compose hiện tại không mở cổng debug. Cách đơn giản để học là chạy `CatalogApplication` từ IDE với profile `local` và các biến DB được cấu hình an toàn, hoặc chạy một test H2 từ IDE.

Đặt breakpoint theo thứ tự:

1. `ProductController.create`: xem request record đã được bind sau khi validation thành công.
2. `ProductApiMapper.toInput`: xem các field được chuyển sang input.
3. `ProductService.create`: xem SKU, điều kiện trùng và transaction boundary. Trong IDE, step vào proxy/framework có thể tạo nhiều stack frame; dùng step over để tập trung business code.
4. `ProductEntity.onCreate` và `ProductResultMapper.toResult`: xem lifecycle callback và dữ liệu response được tạo.
5. `ApiExceptionHandler.invalid` hoặc `notFound`: thử request lỗi để xem code ánh xạ exception sang HTTP.

Đối với request validation lỗi, breakpoint ở dòng đầu controller method **không dừng** vì MVC kiểm tra argument trước; breakpoint tại advice sẽ giúp thấy lỗi. Các repository method do Spring Data tạo không có thân method Java trong interface để step vào như method thường; quan sát service caller, Hibernate logs hoặc dùng debugger trên framework khi cần.

## 10. Lab thực hành

### Lab 1 — Đọc code không cần database

Mở `CreateProductRequest`, `CreateProductInput`, `ProductResult`, `ProductResponse`. Ghi tên các field và trả lời: trường nào có validation? Object nào là HTTP contract? Entity nào được JPA quản lý? Theo mapper để tìm câu trả lời. So sánh với ASP.NET controller request DTO → application input → response DTO.

### Lab 2 — Validation và lỗi qua MockMvc/H2

Trong PowerShell tại repo, chạy `./mvnw.cmd` theo cú pháp Windows:

```powershell
.\mvnw.cmd -Dtest=ProductControllerTest test
.\mvnw.cmd -Dtest=ProductRepositoryTest test
```

Đặt breakpoint tại controller và advice, chạy `invalidBodyReturns400ProblemDetail` từ IDE. Mong đợi test thấy 400 và controller method create không chạy với request không hợp lệ. Test repository dùng H2, không cần Docker/SQL Server; nó không chứng minh SQL Server lock, collation, migration hoặc tương thích dialect.

### Lab 3 — Đọc API đang chạy, không ghi dữ liệu

Nếu container đang chạy ở máy local:

```powershell
curl.exe -i "http://localhost:8080/actuator/health/readiness"
curl.exe -i "http://localhost:8080/api/v1/products?page=0&size=5"
```

Mở Swagger UI, xem `GET /api/v1/products/{id}` và đối chiếu `@PathVariable` với method controller. Health/read-only GET xác nhận được đường HTTP cụ thể tại thời điểm gọi; không chứng minh mọi transaction hoặc hành vi production.

### Lab 4 — Kiểm tra rollback

Chạy `OrderingTransactionTest` trong IDE với profile test. Đọc test `failedSecondLineRollsBackOrderAndEarlierStockReservation`: vì lỗi xảy ra trong transaction, test kiểm tra order không còn và reservation trước đó được rollback. Đặt breakpoint ở `OrderService.create`, `InventoryService.reserve`, rồi lần theo exception. Đây là bằng chứng cho H2 test đó; SQL Server concurrency cần kiểm tra riêng trên môi trường được phép.

## 11. Tài liệu đọc tiếp

- Trong repo: [Java cho C# developer](java-core/java-for-csharp-developers.md), [Spring IoC/DI](spring-core/ioc-di.md), [so sánh JPA và EF Core](dotnet-to-java/efcore-vs-jpa.md), [14 ngày học](dotnet-to-java/roadmap-14-days.md).
- Chính thức: [Oracle Java 21 language updates](https://docs.oracle.com/en/java/javase/21/language/), [Spring Boot 3.5: auto-configuration](https://docs.spring.io/spring-boot/3.5/reference/using/auto-configuration.html), [Spring 6.2 component scanning](https://docs.spring.io/spring-framework/reference/6.2/core/beans/classpath-scanning.html).
- Chính thức: [Spring MVC request body và validation](https://docs.spring.io/spring-framework/reference/6.2/web/webmvc/mvc-controller/ann-methods/requestbody.html), [Spring transaction annotations](https://docs.spring.io/spring-framework/reference/6.2/data-access/transaction/declarative/annotations.html), [Spring Data JPA query methods](https://docs.spring.io/spring-data/jpa/reference/3.5/jpa/query-methods.html).
