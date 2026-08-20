# Job Service

Spring Boot microservice for managing job postings in the recruitment management platform.

## Key Features

- Create job postings
- List all jobs
- List jobs by organization
- Update job status
- JWT-based request authentication

## Tech Stack

- Java 21, Spring Boot 4.1.0
- Spring Web, Spring Data JPA, Spring Security
- PostgreSQL + Flyway migrations
- JWT (jjwt 0.12.6)
- Lombok
- Maven

## Prerequisites

- Java 21
- Docker (for PostgreSQL)

## Setup

Start the database:

```bash
docker compose up -d
```

This starts PostgreSQL on port **5434**.

Run the application with the `dev` profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The service starts on port **8082**.

## API Documentation

Interactive Swagger UI is available at:

```
http://localhost:8082/docs
```

Raw OpenAPI spec: `http://localhost:8082/v3/api-docs`

## Environment Variables

Set these before running with the `dev` profile (see `src/main/resources/application-dev.yaml`):

| Variable | Purpose |
|---|---|
| `JWT_SECRET` | JWT signing secret, used to validate tokens issued by authentication-service |

## API Endpoints

Base path: `/api/v1/jobs`

| Method | Path | Description |
|---|---|---|
| POST | `/api/v1/jobs` | Create a job |
| GET | `/api/v1/jobs` | List all jobs |
| GET | `/api/v1/jobs/organization` | List jobs for the requesting organization |
| PATCH | `/api/v1/jobs/{jobId}/status` | Update job status |

## Database

PostgreSQL, managed with Flyway migrations in `src/main/resources/database/migration`:

- `V1__create_jobs_table.sql`
