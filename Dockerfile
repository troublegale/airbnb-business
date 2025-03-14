FROM gradle:jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY .env .env
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8484
