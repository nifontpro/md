version=1.0.0
image='md_client'
#export SPRING_PROFILES_ACTIVE='main, remote'
../../gradlew clean bootBuildImage
docker push 8881981/$image:$version
ssh nifont@nmedalist.ru << EOF
  docker pull 8881981/$image:$version
  cd ~/v1/md;
  docker compose down
  docker compose up -d
EOF
