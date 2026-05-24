#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"

if ! command -v rg >/dev/null 2>&1; then
  echo "Требуется rg (ripgrep) для smoke-check." >&2
  exit 1
fi

run_maven() {
  if mvn "$@"; then
    return 0
  fi

  echo "[retry] Maven упал. Повторяю с TLS-workaround для учебного окружения." >&2
  mvn \
    -Dmaven.resolver.transport=wagon \
    -Dmaven.wagon.http.ssl.insecure=true \
    -Dmaven.wagon.http.ssl.allowall=true \
    "$@"
}

run_case() {
  local name="$1"
  shift

  local outfile
  outfile="$(mktemp)"
  trap 'rm -f "${outfile}"' RETURN

  echo "[case] ${name}"
  (cd "${PROJECT_DIR}" && "$@") >"${outfile}" 2>&1
  cat "${outfile}"

  for pattern in "${EXPECT[@]}"; do
    if ! rg -F --quiet "${pattern}" "${outfile}"; then
      echo "[fail] ${name}: не найдено ожидание: ${pattern}" >&2
      return 1
    fi
  done

  echo "[ok] ${name}"
  rm -f "${outfile}"
  trap - RETURN
}

EXPECT=(
  "Tests run: 2, Failures: 0, Errors: 0, Skipped: 0"
)
run_case "unit-welcome-service" bash -lc "run_maven() { if mvn \"\$@\"; then return 0; fi; echo '[retry] Maven упал. Повторяю с TLS-workaround для учебного окружения.' >&2; mvn -Dmaven.resolver.transport=wagon -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true \"\$@\"; }; run_maven -pl greeting-demo-app -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=WelcomeServiceTest test"

EXPECT=(
  "Tests run: 2, Failures: 0, Errors: 0, Skipped: 0"
)
run_case "starter-unit" bash -lc "run_maven() { if mvn \"\$@\"; then return 0; fi; echo '[retry] Maven упал. Повторяю с TLS-workaround для учебного окружения.' >&2; mvn -Dmaven.resolver.transport=wagon -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true \"\$@\"; }; run_maven -pl greeting-spring-boot-starter-autoconfigure -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=DefaultGreetingServiceTest test"

EXPECT=(
  "Tests run: 1, Failures: 0, Errors: 0, Skipped: 0"
)
run_case "minimal-spring-context" bash -lc "run_maven() { if mvn \"\$@\"; then return 0; fi; echo '[retry] Maven упал. Повторяю с TLS-workaround для учебного окружения.' >&2; mvn -Dmaven.resolver.transport=wagon -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true \"\$@\"; }; run_maven -pl greeting-demo-app -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=NotificationConfigurationContextTest test"

EXPECT=(
  "Tests run: 1, Failures: 0, Errors: 0, Skipped: 0"
)
run_case "full-boot-context" bash -lc "run_maven() { if mvn \"\$@\"; then return 0; fi; echo '[retry] Maven упал. Повторяю с TLS-workaround для учебного окружения.' >&2; mvn -Dmaven.resolver.transport=wagon -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true \"\$@\"; }; run_maven -pl greeting-demo-app -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=WelcomeServiceSpringBootTest test"

EXPECT=(
  "Tests run: 1, Failures: 0, Errors: 0, Skipped: 0"
)
run_case "inline-properties-boot-context" bash -lc "run_maven() { if mvn \"\$@\"; then return 0; fi; echo '[retry] Maven упал. Повторяю с TLS-workaround для учебного окружения.' >&2; mvn -Dmaven.resolver.transport=wagon -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true \"\$@\"; }; run_maven -pl greeting-demo-app -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=WelcomeServiceInlinePropertiesSpringBootTest test"

EXPECT=(
  "Tests run: 1, Failures: 0, Errors: 0, Skipped: 0"
)
run_case "limited-boot-context" bash -lc "run_maven() { if mvn \"\$@\"; then return 0; fi; echo '[retry] Maven упал. Повторяю с TLS-workaround для учебного окружения.' >&2; mvn -Dmaven.resolver.transport=wagon -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true \"\$@\"; }; run_maven -pl greeting-demo-app -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=WelcomeServiceLimitedContextSpringBootTest test"

EXPECT=(
  "Tests run: 4, Failures: 0, Errors: 0, Skipped: 0"
  "Spring test ApplicationContext cache statistics"
)
run_case "context-cache-observation" bash -lc "run_maven() { if mvn \"\$@\"; then return 0; fi; echo '[retry] Maven упал. Повторяю с TLS-workaround для учебного окружения.' >&2; mvn -Dmaven.resolver.transport=wagon -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true \"\$@\"; }; run_maven -pl greeting-demo-app -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=ContextCachingObservationTest -Dlogging.level.org.springframework.test.context.cache=DEBUG test"

echo "[done] Все тестовые smoke-сценарии прошли"
