podman network create data-flow

podman run -it --rm \
  --name spark-shell \
  --net data-flow \
  -p 4040:4040 \
  apache/spark-py:v3.4.0 \
  /opt/spark/bin/pyspark \
  --packages org.apache.hadoop:hadoop-aws:3.3.4,com.amazonaws:aws-java-sdk-bundle:1.12.262

#
#podman run -it --rm \
#  --name s3 \
#  --net data-flow \
#  -p 9000:9000 \
#  -p 9001:9001 \
#  -e "MINIO_ROOT_USER=admin" \
#  -e "MINIO_ROOT_PASSWORD=password123" \
#  -v minio_data:/data \
#  quay.io/minio/minio server /data --console-address ":9001"