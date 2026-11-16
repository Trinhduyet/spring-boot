---
name: gitlab-ci-docker
description: Legacy alias; use gitlab-docker for the basic Maven, Docker Compose, and registry workflow.
---

# Basic GitLab CI and Docker

## Trigger
Use when changing the five-stage learning pipeline, local Compose file, or container registry publishing.

## Workflow
1. Keep validate, test, build, docker-build, and docker-push stages.
2. Run H2 tests without SQL Server or Docker.
3. Tag images with commit SHA and use GitLab registry variables.
4. Keep credentials out of source and logs; deployment is outside this learning scope.

## Checklist and acceptance
- [ ] CI YAML has exactly the five learning stages.
- [ ] Image build and push are separate.
- [ ] Compose starts only the API and connects to an existing SQL Server.
- [ ] No deployment or monitoring stack is introduced.

## Example
Check `.gitlab-ci.yml`, `docker-compose.yml`, `.env.example`, and run `./mvnw clean test`.
