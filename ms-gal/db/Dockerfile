FROM openjdk:17

RUN mkdir /app

WORKDIR /app
COPY build/libs/gallery.jar /app/gallery.jar

CMD ["java", "-Dspring.profiles.active=main, remote", "-server", "-jar", "/app/gallery.jar"]