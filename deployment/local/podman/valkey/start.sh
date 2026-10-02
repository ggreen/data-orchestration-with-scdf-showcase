podman network create valkey
podman run -it --rm --network=valkey\
  --name valkey \
  --hostname valkey \
  -p 6379:6379 \
  valkey/valkey:latest
