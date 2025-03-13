FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar /app/app.jar
COPY .env /app/.env
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8484