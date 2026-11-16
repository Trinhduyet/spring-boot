# ADR 0003: Basic Spring Security for learning

- Status: Accepted for this learning stage.
- Decision: `local` and `test` are isolated permit-all profiles; other profiles use stateless HTTP Basic with a BCrypt-encoded in-memory user. Development credentials come from environment variables.
- Reason: demonstrate Spring Security filter-chain configuration and 401 behavior without adding an identity provider, JWT issuance, or a user-management subsystem.
- Consequences: this is not production identity management. HTTP Basic requires TLS on shared networks; local profile must remain loopback-only. Tests cover missing, valid, and invalid credentials.


