# Basic GitLab CI and Docker

The pipeline has five stages: `validate`, `test`, `build`, `docker-build`, and `docker-push`. The test stage runs Maven Wrapper tests with H2; it does not start SQL Server or require Docker. Docker jobs use a runner that supports Docker-in-Docker and publish an image tagged with the commit SHA to GitLab Container Registry.

Local `docker-compose.yml` starts the API and connects to the existing SQL Server using values in an ignored `.env`. It does not create a database. The Dockerfile builds the JAR and runs it in a Java 21 runtime image. Compose health checks the Actuator readiness endpoint.

```bash
cp .env.example .env
docker compose up --build
```

Never store real passwords in the image, `.env.example`, CI YAML, or Git. Registry credentials are supplied by GitLab's built-in variables. The pipeline does not deploy to a host; it is a build/publish learning example.

**Pitfalls:** a successful image build does not prove the container can reach SQL Server; a successful H2 test does not prove SQL Server compatibility; a CI YAML parse does not prove GitLab runner configuration.

**Lab:** run `./mvnw clean test`, inspect the image stages, and trace `CI_COMMIT_SHA` into the pushed tag. Source: [pipeline](../../.gitlab-ci.yml), [Dockerfile](../../Dockerfile), [Compose](../../docker-compose.yml).


