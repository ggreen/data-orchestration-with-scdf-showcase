podman run --name mysql -it \
  --network data-orchestration \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
  -e MYSQL_DATABASE=mysql \
  -p 3306:3306 \
  mysql:8.0