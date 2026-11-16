# Basic API authentication

The `local` and `test` profiles permit requests so the API is easy to run and test in isolation. Other profiles use stateless HTTP Basic authentication. In `dev`, the username and password come from `APP_USERNAME` and `APP_PASSWORD`; the password is encoded with BCrypt for the in-memory user store. No user database or token issuer is part of this learning example.

```bash
curl -u "$APP_USERNAME:$APP_PASSWORD" http://localhost:8080/api/v1/products
```

HTTP Basic sends credentials with each request and requires TLS on any shared network. Use the local profile only on loopback; do not treat this sample configuration as production identity management.

**Pitfalls:** do not commit credentials; do not expose the local permit-all profile; do not confuse HTTP Basic with OAuth/OIDC or a token lifecycle.

**Lab:** test a missing and valid credential in `ProductSecurityTest`; explain the 401 response and compare it with ASP.NET Core authentication middleware. Source: [SecurityConfig](../../src/main/java/com/example/catalog/security/infrastructure/config/SecurityConfig.java), [ProductSecurityTest](../../src/test/java/com/example/catalog/security/infrastructure/config/ProductSecurityTest.java).


