# Quickstart

## Purpose

Start the local catalog API against an approved SQL Server development database and exercise a real workflow. Copy `.env.example` to `.env` and provide the connection settings first.

```powershell
docker compose up --build -d
Invoke-RestMethod http://localhost:8080/actuator/health/readiness
$product = Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/v1/products -ContentType 'application/json' -Body '{"sku":"KB-101","name":"Keyboard","price":89.90,"active":true}'
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/v1/inventory/$($product.id)/adjust" -ContentType 'application/json' -Body '{"quantity":10,"reference":"opening-stock"}'
Invoke-RestMethod "http://localhost:8080/api/v1/products?search=keyboard&page=0&size=20"
```

Local profile allows anonymous calls for this isolated workstation workflow. The `dev` profile uses HTTP Basic credentials from `APP_USERNAME` and `APP_PASSWORD`. See [API auth](../api/authentication.md).

## Pitfalls

If readiness remains down, inspect `docker compose logs api`. Check SQL Server reachability, credentials, and schema compatibility. Flyway is disabled by default; enable it only after reviewing the scripts and confirming the target schema is appropriate. Existing IDs depend on database state; use the `id` returned by create.


