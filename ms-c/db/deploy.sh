#version=1.0.0
../../gradlew clean bootJar -x test
scp ./build/libs/client.jar nifont@mmedalist.ru:~/v1/md/client/client.jar
#docker build . -t 8881981/rs:$version
#DOCKER_BUILDKIT=1 docker build . --platform=linux/amd64 -t 8881981/mdserver:$version
#docker push 8881981/mdserver:$version