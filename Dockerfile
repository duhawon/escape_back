# ===== 1단계: 빌드 =====
# Gradle로 jar 파일을 만들기
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Gradle 관련 파일 먼저 복사 (캐시 최적화: 의존성 안 바뀌면 재다운로드 안 함)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
# 의존성 미리 다운로드
RUN ./gradlew dependencies --no-daemon || true

# 소스 코드 복사 후 빌드
COPY src src
RUN ./gradlew clean bootJar --no-daemon

# ===== 2단계: 실행 =====
# 실제 서버에 올라갈 가벼운 이미지 (JDK 대신 JRE만)
FROM eclipse-temurin:21-jre
WORKDIR /app

# 위 build 단계에서 만든 jar만 가져옴
COPY --from=build /app/build/libs/*.jar app.jar

# Spring Boot 기본 포트
EXPOSE 8080

# 컨테이너 시작 시 실행할 명령
ENTRYPOINT ["java", "-jar", "app.jar"]