# ADR 0001: Package-by-feature modular monolith

- Status: Accepted
- Context: Catalog, inventory, orders and identity share transactional workflows and are maintained by one product team.
- Decision: Use a modular monolith organized by feature, with layers inside each feature. Add domain objects/ports where workflow complexity warrants them.
- Consequences: Local transactions and simpler operations; boundaries must be enforced in code review and architecture tests. A future service split requires independent ownership, scaling or lifecycle evidence.
- Alternatives: Global layer packages (scatter changes across all features); early microservices (network and distributed transaction costs); full DDD everywhere (unnecessary overhead for simple catalog behavior).


