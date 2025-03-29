FROM gradle:jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew :tt-oauth2-oauth2Authorization-server:clean :tt-oauth2-oauth2Authorization-server:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/tt-oauth2-oauth2Authorization-server/build/libs/tt-oauth2-oauth2Authorization-server-*.jar ./tt-oauth2-oauth2Authorization-server.jar
ENTRYPOINT ["java", "-jar", "tt-oauth2-oauth2Authorization-server.jar", "--spring.config.location=classpath:/application.yml"]