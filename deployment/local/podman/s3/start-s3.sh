podman run -it --rm \
  --name s3 \
  -p 9000:9000 \
  -p 9001:9001 \
  -e "MINIO_ROOT_USER=admin" \
  -e "MINIO_ROOT_PASSWORD=password123" \
  -v minio_data:/data \
  quay.io/minio/minio server /data --console-address ":9001"