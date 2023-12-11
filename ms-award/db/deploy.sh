version=1.0.0
image='md_medal'
../../gradlew clean bootJar
DOCKER_BUILDKIT=1 docker build . --platform=linux/amd64 -t 8881981/$image:$version
docker push 8881981/$image:$version
ssh nifont@nmedalist.ru << EOF
  docker pull 8881981/$image:$version
  cd ~/md;
  docker compose down
  docker compose up -d
EOF
