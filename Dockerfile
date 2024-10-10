FROM gradle:8.10.1-jdk21 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew /app/
COPY gradle /app/gradle

RUN ./gradlew dependencies --no-daemon

COPY . /app

RUN ./gradlew build --no-daemon

FROM eclipse-temurin:21-jdk-alpine

RUN addgroup -S spring
RUN adduser -S spring -G spring

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

RUN chown spring:spring /app/app.jar

USER spring

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
