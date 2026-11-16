# Basic security configuration

Spring Security protects `/api/**` with HTTP Basic and a single in-memory user in the development profile. The username/password are read from environment-backed configuration; BCrypt encodes the password in memory. Health checks are public. The local profile is permit-all for isolated learning and the test profile is isolated for automated tests.

```java
http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
        .requestMatchers("/api/**").hasRole("USER")
        .anyRequest().denyAll())
    .httpBasic(Customizer.withDefaults());
```

ASP.NET Core also builds authentication and authorization from middleware and policies. Here Spring Security installs a filter chain and maps roles to authorities. This small example has no OIDC provider, JWT issuance, user database, refresh tokens, or production identity lifecycle.

**Pitfalls:** HTTP Basic must use TLS outside loopback; do not expose the local permit-all profile or commit credentials.

**Lab:** compare unauthenticated and authenticated requests in `ProductSecurityTest`. Source: [SecurityConfig](../../src/main/java/com/example/catalog/security/infrastructure/config/SecurityConfig.java).


