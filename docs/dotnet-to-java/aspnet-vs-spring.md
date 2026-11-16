# ASP.NET Core vs Spring MVC

ASP.NET Core middleware is an ordered request pipeline; Spring MVC controllers run after servlet filters and handler mapping. Spring Security owns its own filter chain, so place authentication/authorization there rather than adding an ad-hoc controller check. Bind config using validated `@ConfigurationProperties`, validate records at the edge with `@Valid`, and map exceptions to a stable Problem Details contract.

Example: `ProductController` handles HTTP response codes and delegates to `ProductService`; non-local authorization lives in `SecurityConfig`. Beware that component scan starts from the application package and children. Avoid separate top-level packages that require fragile manual scanning.


