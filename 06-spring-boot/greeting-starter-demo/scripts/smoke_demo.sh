#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
JAR_PATH="${PROJECT_DIR}/greeting-demo-app/target/greeting-demo-app-1.0.0.jar"

if ! command -v rg >/dev/null 2>&1; then
  echo "Требуется rg (ripgrep) для smoke-check." >&2
  exit 1
fi

build_if_needed() {
  if [[ ! -f "${JAR_PATH}" ]]; then
    echo "[build] jar не найден, запускаю mvn package"
    (cd "${PROJECT_DIR}" && mvn -q -DskipTests package)
  fi
}

if [[ "${1:-}" == "--build" ]]; then
  echo "[build] явная пересборка demo"
  (cd "${PROJECT_DIR}" && mvn -q -DskipTests package)
else
  build_if_needed
fi

run_case() {
  local name="$1"
  shift

  local outfile
  outfile="$(mktemp)"
  trap 'rm -f "${outfile}"' RETURN

  echo "[case] ${name}"
  (cd "${PROJECT_DIR}" && "$@") >"${outfile}" 2>&1
  cat "${outfile}"

  shift 0
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
  "profiles.active=[]"
  "beans.greeting=[greetingService]"
  "beans.notification=[noopNotificationService]"
  "beans.devBanner=[]"
  "beans.experimental=[]"
  "Welcome! Hello, DRIP!"
)
run_case "default" java -jar "${JAR_PATH}"

EXPECT=(
  "profiles.active=[]"
  "beans.notification=[realNotificationService]"
  "[NOTIFY] Welcome! Hello, DRIP!"
)
run_case "feature.notify=true" java -jar "${JAR_PATH}" --feature.notify=true

EXPECT=(
  "profiles.active=[dev]"
  "beans.notification=[devNoopNotificationService]"
  "beans.devBanner=[devBanner]"
  "=== DEV MODE ==="
  "Добро пожаловать! Привет, DRIP!"
)
run_case "profile=dev" java -jar "${JAR_PATH}" --spring.profiles.active=dev

EXPECT=(
  "profiles.active=[]"
  "demo.locale=ru resolvedLocale=ru"
  "Добро пожаловать! Hello, DRIP!"
)
run_case "demo.locale=ru" java -jar "${JAR_PATH}" --demo.locale=ru

EXPECT=(
  "profiles.active=[]"
  "beans.experimental=[experimentalFeature]"
  "[EXPERIMENTAL] включено через чистый Spring @Conditional"
)
run_case "feature.experimental=true" java -jar "${JAR_PATH}" --feature.experimental=true

EXPECT=(
  "profiles.active=[dev]"
  "beans.notification=[realNotificationService]"
  "beans.devBanner=[devBanner]"
  "beans.experimental=[experimentalFeature]"
  "Добро пожаловать! Привет, DRIP!"
  "[NOTIFY] Добро пожаловать! Привет, DRIP!"
)
run_case "combined" java -jar "${JAR_PATH}" --feature.notify=true --spring.profiles.active=dev --demo.locale=ru --feature.experimental=true

EXPECT=(
  "CONDITIONS EVALUATION REPORT"
  "GreetingAutoConfiguration"
  "MessageSourceAutoConfiguration matched:"
)
run_case "debug" bash -lc "java -jar '${JAR_PATH}' --debug 2>&1 | rg 'CONDITIONS EVALUATION REPORT|GreetingAutoConfiguration|MessageSourceAutoConfiguration'"

echo "[done] Все smoke-сценарии прошли"
