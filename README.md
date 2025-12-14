# Nexus

High-Throughput Task Scheduler and Monitoring System (MVP scaffold).

This repo contains:

- `scheduler-service/` (Spring Boot): job submission + scheduling + WebSocket updates
- `frontend/` (Next.js): live monitoring dashboard (Jobs + Workers)
- `cpp-worker/` (C++): a polling worker that executes simulated compute and reports progress

## MVP architecture (implemented)

- Jobs are submitted to the scheduler via REST.
- A worker registers and polls the scheduler for work (MVP transport).
- The worker reports `STATUS`, `PROGRESS`, and `HEARTBEAT` events back to the scheduler.
- The scheduler persists state to Postgres and pushes live updates to the UI via WebSocket (STOMP).

Note: the broker (Kafka/RabbitMQ) integration is intentionally deferred; the code is structured so that the polling transport can be swapped for a broker-backed transport without changing the UI contract.

## Local dev (Docker Compose)

Prereqs:

- Docker Desktop
- Node.js 20+
- JDK 21+
- A C++17 toolchain (MSVC or MinGW) if you want to build the worker locally

### 1) Start Postgres + scheduler + frontend

From repo root:

`docker compose up --build`

URLs:

- Scheduler REST: `http://localhost:8080`
- Scheduler WS (STOMP): `ws://localhost:8080/ws`
- Frontend: `http://localhost:3000`

### 2) Run a worker locally

Build and run:

- `cmake -S cpp-worker -B cpp-worker/build`
- `cmake --build cpp-worker/build --config Release`
- `cpp-worker\\build\\Release\\cpp-worker.exe --worker-id worker-01 --scheduler http://localhost:8080`

### 3) Submit a job

Use the UI (Jobs page) or curl:

`curl -X POST http://localhost:8080/api/jobs -H "Content-Type: application/json" -d "{\"parameters\":{\"steps\":120,\"sleepMs\":50}}"`

## API surface (MVP)

- `POST /api/jobs` submit job
- `GET /api/jobs` list jobs
- `GET /api/jobs/{id}` get job
- `POST /api/workers/register` register worker
- `GET /api/workers/{workerId}/next-command` worker polls for next command
- `POST /api/workers/{workerId}/events` worker reports heartbeat/status/progress

## Job lifecycle

`CREATED -> QUEUED -> RUNNING -> COMPLETED | FAILED | CANCELED`

Only the scheduler changes job state. Workers emit events.