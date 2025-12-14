FROM alpine:3.20
RUN apk add --no-cache ca-certificates curl
COPY build/cpp-worker /usr/local/bin/cpp-worker
ENTRYPOINT ["/usr/local/bin/cpp-worker"]
