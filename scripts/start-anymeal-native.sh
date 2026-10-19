#!/usr/bin/env bash
# .env 설정을 불러와 네이티브 애플리케이션을 시작한다.
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
APP_PATH="$SCRIPT_DIR/any-meal"
ENV_FILE="$SCRIPT_DIR/.env"

if [[ -f "$ENV_FILE" ]]; then
  set -a
  source "$ENV_FILE"
  set +a
fi

if [[ ! -x "$APP_PATH" ]]; then
  echo "Native executable not found or not executable: $APP_PATH" >&2
  exit 1
fi

cd "$SCRIPT_DIR"
nohup "$APP_PATH" >> "$SCRIPT_DIR/any-meal.log" 2>&1 < /dev/null &
echo "$!" > "$SCRIPT_DIR/any-meal.pid"
