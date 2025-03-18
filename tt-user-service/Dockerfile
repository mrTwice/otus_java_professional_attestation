FROM gradle:jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew :tt-user-service:clean :tt-user-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/tt-user-service/build/libs/tt-user-service-*.jar ./tt-user-service.jar
ENTRYPOINT ["java", "-jar", "tt-user-service.jar", "--spring.config.location=classpath:/application.yml"]