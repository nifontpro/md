FROM openjdk:18

RUN mkdir /app

WORKDIR /app
COPY ./server.jar /app/server.jar

# CMD ["java","-server","-Dspring.profiles.active=db","-jar","/app/server.jar"]
CMD ["java","-Dspring.profiles.active=db","-jar","/app/server.jar"]

