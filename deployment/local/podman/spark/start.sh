podman network create data-flow

podman run -it --rm \
  --name spark-pyspark \
  --net data-flow \
  apache/spark-py:v3.4.0 \
  /opt/spark/bin/pyspark \
  --packages org.apache.iceberg:iceberg-spark-runtime-3.4_2.12:1.3.1,org.apache.hadoop:hadoop-aws:3.3.4,com.amazonaws:aws-java-sdk-bundle:1.12.262
