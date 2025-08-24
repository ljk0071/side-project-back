#!/bin/sh

# 종료 신호를 처리할 함수 정의
shutdown() {
  echo "👋 Shutting down Valkey server..."
  # 저장해둔 PID로 valkey-server에 SIGTERM 신호를 보냄
  kill -TERM "$server_pid"
  # 자식 프로세스가 완전히 종료될 때까지 기다림
  wait "$server_pid"
}

# 1. Valkey 서버를 백그라운드(&)로 실행합니다.
valkey-server \
    --port 6379 \
    --appendonly yes \
    --requirepass ghbndfjhgdfjk \
    --masterauth ghbndfjhgdfjk &

# 2. 백그라운드로 실행된 서버의 프로세스 ID(PID)를 저장합니다.
server_pid=$!

# ✨✨ 추가된 부분 ✨✨
# SIGTERM 신호를 받으면 shutdown 함수를 실행하도록 설정 (trap)
trap shutdown TERM

# 3. 서버가 준비될 때까지 대기합니다.
echo "Waiting for Valkey server to start..."
while ! valkey-cli -h 127.0.0.1 -p 6379 -a ghbndfjhgdfjk ping > /dev/null 2>&1; do
    sleep 1
done
echo "✅ Valkey server is ready."

# 4. 초기화 스크립트를 실행합니다.
echo "Running initialization script..."
valkey-cli -h 127.0.0.1 -p 6379 -a ghbndfjhgdfjk < /init/init_script.redis
echo "✅ Initialization complete."

# 5. 이제 wait 명령은 스크립트가 종료 신호를 받을 때까지 여기서 대기합니다.
#    신호를 받으면 위에서 설정한 trap이 shutdown 함수를 실행합니다.
wait "$server_pid"