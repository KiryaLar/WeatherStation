FROM openjdk:21-jdk-slim

WORKDIR /app
COPY target/consumer.jar /app/consumer.jar

RUN mkdir -p /app/logs
VOLUME /app/logs

ENTRYPOINT ["java", "-jar", "/app/consumer.jar"]