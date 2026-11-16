# Installation

Clone the repository, copy `.env.example` to `.env`, fill in the approved SQL Server development connection, then start the API:

```powershell
Copy-Item .env.example .env
docker compose up --build -d
docker compose ps
```

The database uses demo-only local credentials and ports bind to loopback. Do not expose this Compose stack to a network. To run from an IDE, start `docker compose up -d db`, set `SPRING_PROFILES_ACTIVE=local`, then run `./mvnw spring-boot:run` (Windows: `./mvnw.cmd spring-boot:run`).

Stop services without deleting data using `docker compose down`; add `-v` only when intentionally removing the local database volume.


