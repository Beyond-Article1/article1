# 빌드 단계
FROM node:lts-alpine AS builder

# app 디렉토리 생성
WORKDIR /app

# package.json 및 lock 파일을 복사하여 종속성 설치 (*로 모든걸 선택)
COPY package*.json ./
RUN npm install

COPY .env .env

# 소스 파일 복사 및 빌드
COPY . .
RUN npm run build

# 실행 단계
FROM nginx:latest

# FE 디렉토리의 nginx.conf 파일을 /etc/nginx/nginx.conf로 복사
COPY ./nginx.conf /etc/nginx/nginx.conf

# Nginx를 통해 빌드된 정적 파일을 제공
COPY --from=builder /app/dist /usr/share/nginx/html

# Nginx 기본 포트를 노출
EXPOSE 80

# Nginx 기본 명령으로 실행
CMD ["nginx", "-g", "daemon off;"]
