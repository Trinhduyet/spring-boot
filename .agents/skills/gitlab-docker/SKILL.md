---
name: gitlab-docker
description: Maintain basic Maven, Docker, Docker Compose, and GitLab Container Registry workflows.
---

# Basic GitLab CI and Docker

## Trigger
Use when changing the learning pipeline, image, Compose setup, or local Docker workflow.

## Workflow
1. Keep validation, tests, JAR build, image build, and image push as separate stages.
2. Use Maven Wrapper and Java 21; tests must not require SQL Server or Docker.
3. Tag images with immutable commit SHA and GitLab registry credentials.
4. Keep local configuration in `.env.example`; never commit credentials.

```yaml
test:
  stage: test
  image: maven:3.9-eclipse-temurin-21
  script: ["./mvnw -B -ntp clean test"]
```

## Checklist
- [ ] Pipeline matches the learning scope.
- [ ] H2 test job runs without service containers.
- [ ] Image build and push use CI registry variables.
- [ ] No Jenkins, SonarQube, Kubernetes, or Helm is introduced.

## Check
Parse YAML, inspect artifacts and job ordering, run tests, and validate Compose when Docker is available.
