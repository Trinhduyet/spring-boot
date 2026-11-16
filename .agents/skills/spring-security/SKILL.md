---
name: spring-security
description: Configure or review the project's basic Spring Security filter chains and HTTP Basic tests.
---

# Basic Spring Security workflow

1. Keep `local`/`test` isolated and permit-all; protect `dev` routes with HTTP Basic.
2. Read credentials from environment configuration and BCrypt-encode them in memory.
3. Permit health checks and deny routes outside the intended API.
4. Add tests for missing credentials, valid credentials, and invalid passwords.
5. State clearly that this example is not production identity management.

## Checklist

- [ ] Shared profiles cannot disable authentication.
- [ ] No password is hardcoded or committed.
- [ ] Local permit-all is restricted to isolated development.
- [ ] HTTP Basic use outside loopback is protected by TLS.
- [ ] Authentication errors are safe and useful.

References: `SecurityConfig`, `docs/architecture/security-architecture.md`, `docs/api/authentication.md`.
