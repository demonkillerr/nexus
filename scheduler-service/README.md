# Nexus Scheduler Service

Spring Boot 3.3 scheduling gateway and control plane.

## Prerequisites

- JDK 21+
- Postgres running on localhost:5432 (or use docker-compose)

## Dev

```bash
./mvnw spring-boot:run
```

Or on Windows:

```cmd
mvnw.cmd spring-boot:run
```

API: http://localhost:8080

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | /api/jobs | Submit a job |
| GET | /api/jobs | List jobs |
| GET | /api/jobs/{id} | Get job by ID |
| POST | /api/workers/register | Register a worker |
| GET | /api/workers | List workers |
| GET | /api/workers/{id}/next-command | Worker polls for work |
| POST | /api/workers/{id}/events | Worker reports heartbeat/status/progress |

## WebSocket

STOMP endpoint: `ws://localhost:8080/ws`

Topics:
- `/topic/jobs` - all job updates
- `/topic/jobs/{id}` - single job updates
- `/topic/workers` - worker status updates

## Docker

```bash
docker build -t nexus-scheduler .
docker run -p 8080:8080 --env SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/nexus nexus-scheduler
```
