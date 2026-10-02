podman network create valkey

podman run -it --rm --network=valkey\
  --name valkey-admin \
  -p 7380:8080 \
  docker.io/valkey/valkey-admin:latest