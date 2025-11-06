FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies
COPY src src
RUN ./gradlew build -x test

FROM eclipse-temurin:21-jre-jammy AS runner
WORKDIR /app

VOLUME /tmp
COPY --from=builder /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
