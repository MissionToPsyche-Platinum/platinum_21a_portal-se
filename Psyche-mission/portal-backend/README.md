# Psyche Portal backend

Spring Boot API for the Psyche Mission game portal. It talks to PostgreSQL and is used with the Vue app in `../portal-frontend`.

- API: [http://localhost:8080/](http://localhost:8080/)
- Frontend (run separately): [http://localhost:5173/](http://localhost:5173/)

## Requirements

- [JDK 21](https://adoptium.net/) (Temurin is fine)
- [PostgreSQL](https://www.postgresql.org/download/) 16+ with the service running
- Git

The Gradle wrapper in this folder (`gradlew` / `gradlew.bat`) downloads Gradle for you.

## 1. Create the database (once)

From this `portal-backend` directory:

```sh
psql postgres -f database/setup.sql
```

On Windows, if `psql` is not on your PATH:

```powershell
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -f database/setup.sql
```

That creates the `psyche_portal` database. If it already exists, PostgreSQL may print that the database already exists; that is fine.

More detail: [database/README.md](database/README.md).

On startup, Flyway applies migrations (users/roles and related tables). Hibernate is set to `validate`, so the schema must match the entities.

## 2. Set database credentials

`application.properties` reads:

| Variable       | Default                                      | Typical local value                         |
|----------------|----------------------------------------------|---------------------------------------------|
| `DB_URL`       | `jdbc:postgresql://localhost:5432/psyche_portal` | leave unset unless Postgres is not local |
| `DB_USERNAME`  | `babikerbabiker`                             | your Postgres username (often `postgres`)   |
| `DB_PASSWORD`  | empty                                        | your Postgres password                      |

Do not commit real passwords. Set them in the terminal before `bootRun`.

## 3. Run the backend

**Windows (PowerShell):**

```powershell
cd Psyche-mission/portal-backend
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "your-postgres-password"
.\gradlew.bat bootRun
```

Change `JAVA_HOME` if JDK 21 is installed somewhere else.

**macOS / Linux:**

```sh
cd Psyche-mission/portal-backend
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # macOS; or set the JDK 21 path
export DB_USERNAME=postgres
export DB_PASSWORD=your-postgres-password
./gradlew bootRun
```

Leave this terminal open. Confirm it started:

- [http://localhost:8080/](http://localhost:8080/)
- [http://localhost:8080/api/test/status](http://localhost:8080/api/test/status)

## 4. Run the frontend (second terminal)

The UI proxies `/api` to port 8080 (see `portal-frontend/vite.config.js`).

```sh
cd Psyche-mission/portal-frontend
npm install
npm run dev
```

Open [http://localhost:5173/](http://localhost:5173/).

## Useful API paths

| Method | Path | Notes |
|--------|------|--------|
| GET | `/` | API root |
| GET | `/api/test/status` | health check |
| GET | `/api/games` | game catalog |
| POST | `/api/users` | sign up |
| POST | `/api/users/login` | login |
| GET | `/api/game-upload-requests` | list upload requests |
| GET | `/api/game-upload-requests/pending` | admin; send header `X-Role: admin` |

## Tests

```sh
./gradlew test
```

Windows: `.\gradlew.bat test`

## Troubleshooting

- **Wrong Java version:** `JAVA_HOME` must point at JDK 21.
- **Database auth failed:** set `DB_USERNAME` and `DB_PASSWORD` to match your local Postgres user.
- **`psyche_portal` does not exist:** run `database/setup.sql` again.
- **Port 8080 in use:** stop the other process, or change the port in `application.properties`.
- **Frontend cannot reach the API:** start this backend first, then Vite on 5173.
