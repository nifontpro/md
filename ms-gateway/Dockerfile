FROM openjdk:17
FROM bellsoft/liberica-openjdk-debian:17.0.9

RUN mkdir /app

WORKDIR /app
COPY build/libs/gateway.jar /app/gateway.jar

CMD ["java", "-Dspring.profiles.active=main, remote", "-server", "-jar", "/app/gateway.jar"]