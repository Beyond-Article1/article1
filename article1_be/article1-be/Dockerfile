# 빌드 단계
FROM openjdk:17-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew bootjar

# 실행 단계
FROM openjdk:17-alpine
WORKDIR /app
EXPOSE 8080

# dockerize 설치
RUN apk add --no-cache curl && \
    curl -sSL https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-alpine-linux-amd64-v0.6.1.tar.gz | tar -C /usr/local/bin -xzv

# 빌드된 JAR 파일을 복사
ARG JAR_FILE=build/libs/*.jar
COPY --from=builder /app/${JAR_FILE} app.jar

# ENTRYPOINT 설정
ENTRYPOINT ["java", "-jar", "app.jar"]
