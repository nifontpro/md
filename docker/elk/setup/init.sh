# Перед первым запуском docker compose up -d выполнить команды:

chmod +x entrypoint.sh
chmod +x helpers.sh

sudo chmod 775 -R data
sudo chown -R $USER:docker data
