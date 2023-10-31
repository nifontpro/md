FROM openjdk:17

RUN mkdir /app

WORKDIR /app
COPY build/libs/shop.jar /app/shop.jar

CMD ["java", "-Dspring.profiles.active=main, remote", "-server", "-jar", "/app/shop.jar"]