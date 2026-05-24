#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DOCKER_CFG="${DOCKER_CONFIG:-/tmp/codex-docker-config}"

mkdir -p "$DOCKER_CFG"
if [[ ! -f "$DOCKER_CFG/config.json" ]]; then
  printf '{}\n' > "$DOCKER_CFG/config.json"
fi

cleanup() {
  cd "$ROOT"
  DOCKER_CONFIG="$DOCKER_CFG" docker-compose down -v --remove-orphans >/dev/null 2>&1 || true
}

trap cleanup EXIT

cd "$ROOT"

"$ROOT/scripts/build_demo.sh"

run_and_assert() {
  local profile="$1"
  local output

  output="$("$ROOT/scripts/run_profile.sh" "$profile")"
  printf '%s\n' "$output"
  printf '%s\n' "$output" | grep -F "Store profile = $profile" >/dev/null
  printf '%s\n' "$output" | grep -F "Notes for owner 'alice' = 2" >/dev/null
}

run_and_assert keyvalue
run_and_assert redis
run_and_assert mongo
