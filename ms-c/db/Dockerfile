FROM openjdk:17

RUN mkdir /app

WORKDIR /app
COPY build/libs/client.jar /app/client.jar

CMD ["java", "-Dspring.profiles.active=main, remote", "-server", "-jar", "/app/client.jar"]