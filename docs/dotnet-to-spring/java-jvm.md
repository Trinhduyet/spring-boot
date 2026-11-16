# Java 21 and JVM operation for .NET developers

Java is compiled to JVM bytecode and runs under a managed runtime. The JDK includes compiler and developer tools; a JRE image is sufficient for the packaged application. Spring Boot 3 uses Java 17+ baselines; this repository targets Java 21.

The JVM manages heap and garbage collection; do not translate .NET GC tuning settings directly. Container memory must leave room for metaspace, thread stacks and native buffers in addition to heap. Set container-aware memory limits and validate GC, heap, thread count and startup under representative load before tuning. Use JFR, `jcmd`, thread dumps and heap dumps with the same care as .NET diagnostics, and protect dumps because they may contain user data.

Java threads and executors are explicit concurrency tools. Java 21 virtual threads can help blocking workloads when enabled and measured, but they do not make database pools or downstream services unlimited. Size HikariCP against SQL Server connection limits and bound concurrency.


