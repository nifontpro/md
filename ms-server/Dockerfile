FROM openjdk:17

RUN mkdir /app

WORKDIR /app
COPY build/libs/server.jar /app/server.jar

CMD ["java", "-Dspring.profiles.active=main, remote", "-server", "-jar", "/app/server.jar"]