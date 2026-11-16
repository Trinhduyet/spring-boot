# Spring Boot auto-configuration

`@SpringBootApplication` combines component scanning, configuration, and `@EnableAutoConfiguration`. Boot applies configuration when classpath, property, and bean conditions match. Starters group dependencies; they make auto-configuration eligible but do not mean every feature is enabled. Application beans can override selected defaults.

```java
@Configuration
class TimeConfiguration {
    @Bean Clock clock() { return Clock.systemUTC(); }
}
```

ASP.NET Core uses service registrations and host defaults; Spring adds conditional bean definitions to an application context. Profile-specific files and `@ConditionalOnProperty` support optional configuration:

```java
@Bean
@ConditionalOnProperty(name = "catalog.preview.enabled", havingValue = "true")
PreviewService previewService() { return new PreviewService(); }
```

Use `--debug` to inspect the condition evaluation report when a bean is unexpectedly present or missing.

**Pitfalls:** component scanning starts from the application package; avoid defining a duplicate bean without knowing the Boot condition; do not assume adding a dependency configures credentials or external services.

**Lab:** add a Clock bean and verify injection; launch with `--debug` and locate one matched auto-configuration. Source: [CatalogApplication](../../src/main/java/com/example/catalog/CatalogApplication.java).


