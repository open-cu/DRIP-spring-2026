#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

if mvn -q -DskipTests package; then
  exit 0
fi

mvn -q \
  -Dmaven.resolver.transport=wagon \
  -Dmaven.wagon.http.ssl.insecure=true \
  -Dmaven.wagon.http.ssl.allowall=true \
  -DskipTests \
  package
