FROM openjdk:21-jdk-slim

WORKDIR /app
COPY target/producer.jar /app/producer.jar

ENTRYPOINT ["java", "-jar", "producer.jar"]