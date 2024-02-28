version=1.0.0
image='md_server'
../gradlew clean bootJar -x test
#scp ./build/libs/client.jar nifont@mmedalist.ru:~/v1/md/client/client.jar
#docker build . -t 8881981/rs:$version
DOCKER_BUILDKIT=1 docker build . --platform=linux/amd64 -t 8881981/$image:$version
docker push 8881981/$image:$version
ssh root@medalists.ru << EOF
  docker pull 8881981/$image:$version
#  cd ~/md;
#  docker compose up -d
EOF
