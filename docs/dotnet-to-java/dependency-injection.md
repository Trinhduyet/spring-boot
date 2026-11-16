# Dependency injection

Spring components are singleton-scoped by default and created by the application context. Constructor injection makes dependencies final and makes unit tests direct. Use `@ConfigurationProperties` for cohesive settings rather than scattering `@Value`. Spring transactions/security are usually implemented by AOP proxies: self-invocation bypasses proxy advice, and `final` methods/classes can constrain proxying in some modes.

Use interfaces when there is a real port, multiple implementation, or stable public boundary. `ProductCatalog` is a cross-feature contract; a one-implementation service with no boundary benefit can remain a concrete class.


