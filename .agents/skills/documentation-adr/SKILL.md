---
name: documentation-adr
description: Use when changing architecture, security, data ownership, API contracts, deployment, or repository documentation.
---

# Documentation and ADR workflow

## Trigger
Use for behavior or decision changes that affect maintainers, operators, API consumers, or onboarding.

## Workflow
1. Inspect the current code and docs before editing.
2. Record consequential choices as ADRs with status, context, decision, and consequences.
3. Update index links, diagrams, runbooks, API/database docs, and examples affected by the change.
4. Distinguish implementation evidence from unverified environment claims.

## Checklist and acceptance
- [ ] No title-only or TODO-only required docs.
- [ ] Links and commands match current SQL Server/GitLab/Compose setup.
- [ ] API/schema behavior is source-accurate.
- [ ] Verification limits are stated.

## Example
A database vendor decision updates the ADR and the learning database notes. Keep default tests on H2 and label vendor-specific behavior as unverified until checked against an approved database.
