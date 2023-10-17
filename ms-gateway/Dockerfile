FROM openjdk:17

RUN mkdir /app

WORKDIR /app
COPY build/libs/gateway.jar /app/gateway.jar

CMD ["java", "-Dspring.profiles.active=main, remote", "-server", "-jar", "/app/gateway.jar"]