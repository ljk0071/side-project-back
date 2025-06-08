FROM gradle:8.12-jdk21 AS build
WORKDIR /app

# 의존성을 먼저 복사하고 캐시하는 레이어
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle
COPY bootstrap/build.gradle ./bootstrap/
COPY security/build.gradle ./security/
COPY domain ./domain
COPY infrastructure ./infrastructure
COPY client ./client

# gradlew 복사 및 실행 권한 설정
COPY gradlew .
RUN chmod +x ./gradlew

# 의존성 다운로드 (캐시 활용을 위해)
RUN ./gradlew dependencies

# 소스 코드 복사 및 빌드
COPY . .
RUN ./gradlew :bootstrap:bootJar

# 런타임 이미지
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 빌드 이미지에서 jar 파일 복사
COPY --from=build /app/bootstrap/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
