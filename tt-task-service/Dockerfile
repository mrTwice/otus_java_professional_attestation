FROM gradle:jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew :tt-task-service:clean :tt-task-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/tt-task-service/build/libs/tt-task-service-*.jar ./tt-task-service.jar
ENTRYPOINT ["java", "-jar", "tt-task-service.jar", "--spring.config.location=classpath:/application.yml"]