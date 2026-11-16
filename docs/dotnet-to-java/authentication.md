# ASP.NET Core and Spring Security basics

ASP.NET Core commonly registers authentication handlers and authorization policies, then runs them in middleware. Spring Security builds a `SecurityFilterChain` bean; request matchers choose anonymous or authenticated routes and authentication populates the `Principal`.

This project permits requests only in isolated `local` and `test` profiles. Other profiles use HTTP Basic and a BCrypt-encoded in-memory user configured through environment variables. This illustrates a filter chain and authentication flow; it is not a production identity system.

```java
http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/actuator/health/**").permitAll()
        .requestMatchers("/api/**").hasRole("USER"))
    .httpBasic(Customizer.withDefaults());
```

**Pitfalls:** HTTP Basic sends credentials on each request, so shared traffic requires TLS; local permit-all must remain loopback-only; do not confuse authentication (who called) with authorization (what they may do).

**Lab:** compare missing credentials (401) and valid credentials in `ProductSecurityTest`. Source: [SecurityConfig](../../src/main/java/com/example/catalog/security/infrastructure/config/SecurityConfig.java), [ProductSecurityTest](../../src/test/java/com/example/catalog/security/infrastructure/config/ProductSecurityTest.java).


