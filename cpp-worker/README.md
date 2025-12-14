# cpp-worker (MVP)

This worker registers itself with the scheduler and polls for work.

Transport (MVP): REST polling using the system `curl` command.

## Build

`cmake -S . -B build`

`cmake --build build --config Release`

## Run

`build\\Release\\cpp-worker.exe --worker-id worker-01 --scheduler http://localhost:8080`
