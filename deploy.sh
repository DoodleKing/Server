#!/bin/bash
echo "Deployment start"

# 현재 실행 중인 .jar 프로세스의 PID를 가져옵니다.
CURRENT_PID=$(pgrep -f "doodleking-0.0.1-SNAPSHOT.jar")
echo "Current PID: $CURRENT_PID"

# 실행 중인 프로세스가 있으면 종료합니다.
if [ -z "$CURRENT_PID" ]; then
    echo "No running process found."
else
    echo "Killing process $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 3
fi

# 새로운 JAR 파일의 경로를 설정합니다.
JAR_PATH="/home/seojkim0128/Server/cicd/build/libs/doodleking-0.0.1-SNAPSHOT.jar"
LOG_FILE="/home/seojkim0128/Server/cicd/deploy.log"

# JAR 파일에 실행 권한을 부여합니다.
chmod +x $JAR_PATH

# JAR 파일을 백그라운드에서 실행합니다.
nohup java -jar $JAR_PATH > $LOG_FILE 2>&1 &
echo "Deployment successful."
exit 0
