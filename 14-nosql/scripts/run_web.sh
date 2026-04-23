#!/usr/bin/env bash
set -euo pipefail

PROFILE="${1:-keyvalue}"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
JAR="$ROOT/target/nosql-switch-demo-1.0.0.jar"
DOCKER_CFG="${DOCKER_CONFIG:-/tmp/codex-docker-config}"
SERVER_PORT="${SERVER_PORT:-8088}"

cd "$ROOT"

mkdir -p "$DOCKER_CFG"
if [[ ! -f "$DOCKER_CFG/config.json" ]]; then
  printf '{}\n' > "$DOCKER_CFG/config.json"
fi

if [[ ! -f "$JAR" ]]; then
  "$ROOT/scripts/build_demo.sh"
fi

wait_for_port() {
  local port="$1"
  local label="$2"

  for _ in {1..30}; do
    if nc -z 127.0.0.1 "$port" >/dev/null 2>&1; then
      return 0
    fi
    sleep 1
  done

  echo "Timed out waiting for $label on port $port" >&2
  exit 1
}

if [[ "$PROFILE" == "redis" || "$PROFILE" == "mongo" ]]; then
  DOCKER_CONFIG="$DOCKER_CFG" docker-compose up -d "$PROFILE"
fi

if [[ "$PROFILE" == "redis" ]]; then
  wait_for_port 6379 redis
fi

if [[ "$PROFILE" == "mongo" ]]; then
  wait_for_port 27017 mongo
fi

java -jar "$JAR" \
  --spring.profiles.active="$PROFILE" \
  --server.port="$SERVER_PORT" \
  --logging.level.root=INFO
