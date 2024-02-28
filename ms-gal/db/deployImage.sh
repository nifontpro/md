version=1.0.0
image='md_gallery'
../../gradlew clean bootBuildImage
docker push 8881981/$image:$version
ssh root@medalists.ru << EOF
  docker pull 8881981/$image:$version
  cd ~/md;
  docker compose down
  docker compose up -d
EOF
