version=1.0.0
image='md_shop'
../../gradlew clean bootBuildImage
docker push 8881981/$image:$version
ssh nifont@nmedalist.ru << EOF
  docker pull 8881981/$image:$version
  cd ~/v1/md;
  docker compose down
  docker compose up -d
EOF
