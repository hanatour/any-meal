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

PORT=8080
port_pids() {
  ss -H -ltnp "sport = :$PORT" 2>/dev/null \
    | sed -n 's/.*pid=\([0-9][0-9]*\).*/\1/p' \
    | sort -u
}
port_is_listening() {
  ss -H -ltn "sport = :$PORT" 2>/dev/null | grep -q .
}

PIDS="$(port_pids)"
if port_is_listening; then
  if [[ -z "$PIDS" ]]; then
    echo "Port $PORT is in use, but its process ID could not be determined." >&2
    exit 1
  fi

  echo "Stopping process(es) listening on port $PORT: ${PIDS//$'\n'/, }"
  while IFS= read -r PID; do
    [[ -n "$PID" ]] && kill -TERM "$PID" || true
  done <<< "$PIDS"

  for _ in {1..10}; do
    port_is_listening || break
    sleep 1
  done

  if port_is_listening; then
    PIDS="$(port_pids)"
    if [[ -z "$PIDS" ]]; then
      echo "Port $PORT is still in use, but its process ID could not be determined." >&2
      exit 1
    fi
    while IFS= read -r PID; do
      [[ -n "$PID" ]] && kill -KILL "$PID" || true
    done <<< "$PIDS"
    sleep 1
  fi

  if port_is_listening; then
    echo "Port $PORT is still in use after stopping its process." >&2
    exit 1
  fi
fi

rm -f "$SCRIPT_DIR/any-meal.pid"
cd "$SCRIPT_DIR"
nohup "$APP_PATH" >> "$SCRIPT_DIR/any-meal.log" 2>&1 < /dev/null &
echo "$!" > "$SCRIPT_DIR/any-meal.pid"
