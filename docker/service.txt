https://habr.com/ru/articles/486200/

docker system df
docker image ls -f dangling=true

docker image rm $(docker image ls -f dangling=true -q)
docker system prune

mac:
https://dev.to/sergej_brazdeikis/install-docker-on-mac-m1-without-docker-desktop-k6o
https://rancherdesktop.io/
brew install --cask docker
