podman run -it --rm \
  --name valkey \
  -p 6379:6379 \
  valkey/valkey:latest
