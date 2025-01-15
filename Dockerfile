FROM openjdk:21-jdk-slim

LABEL authors="seojin"

# 애플리케이션 소스 코드 복사
COPY . /app

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 파일에 실행 권한 부여
RUN chmod +x gradlew

# Gradle Wrapper 사용하여 빌드
RUN ./gradlew build -x test && ls -al build/libs

# 애플리케이션 실행 명령
ENTRYPOINT ["sh", "-c", "java -jar build/libs/doodleking-0.0.1-SNAPSHOT.jar"]
