#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

MVN_BASE=(mvn)
MVN_FALLBACK=(
  mvn
  -Dmaven.resolver.transport=wagon
  -Dmaven.wagon.http.ssl.insecure=true
  -Dmaven.wagon.http.ssl.allowall=true
)

run_maven() {
  if "${MVN_BASE[@]}" "$@"; then
    return 0
  fi

  echo "[warn] default maven command failed, retrying with SSL fallback"
  "${MVN_FALLBACK[@]}" "$@"
}

run_case() {
  local name="$1"
  shift

  echo "[case] $name"
  run_maven "$@"
  echo "[ok] $name"
}

COMMON_ARGS=(-Dsurefire.failIfNoSpecifiedTests=false)

run_case "unit" "${COMMON_ARGS[@]}" -Dtest=WelcomeServiceUnitTest test
run_case "minimal-context" "${COMMON_ARGS[@]}" -Dtest=NotificationConfigurationContextTest test
run_case "boot4-mockitobean" "${COMMON_ARGS[@]}" -Dtest=WelcomeServiceBoot4MockitoBeanTest test
run_case "boot4-mockitospybean" "${COMMON_ARGS[@]}" -Dtest=WelcomeServiceBoot4MockitoSpyBeanTest test

echo "[done] Все smoke-сценарии modern demo прошли"
