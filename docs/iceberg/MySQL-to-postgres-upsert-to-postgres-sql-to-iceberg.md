# MySQL to Postgres Upsert to Iceberg Sink Pipeline

Building Project
Download (first time only)

```shell
git clone https://github.com/ggreen/data-orchestration-with-scdf-showcase.git
cd data-orchestration-with-scdf-showcase
```
Build (first time only)

```shell
mvn -Dmaven.test.skip=true package
```
Create podman network (if it does not exist)

```shell
podman network create data-orchestration
```

Run RabbitMQ (guest/guest) if not running

```shell
deployment/local/podman/rabbit/start.sh
```

Infrastructure Setup (MySQL, Postgres, MinIO)
Start MySQL for Financial Trade Settlement records:

```shell
podman run --name mysql -it \
  --network data-orchestration \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
  -e MYSQL_DATABASE=mysql \
  -p 3306:3306 \
  mysql:8.0
```

Start Postgres for trade position updates/upserts:

```shell
deployment/local/podman/postgres/start.sh
```

Start MinIO for Iceberg S3 Data Lake storage:

```shell
deployment/local/podman/s3/start-s3.sh
```

Start SCDF
Start Skipper (if not running)

```shell
deployment/local/dataflow/start-skipper.sh
```
Start Data Flow Server (if not running)

```shell
deployment/local/dataflow/start-df-server.sh
```

Financial Database Schema Setup
1. MySQL (Source Trade Settlements)
Connect to MySQL container:

```shell
podman exec -it mysql mysql -uroot -proot mysql
```

Create schema and insert sample Financial trade settlement data:

```SQL
CREATE TABLE trade_settlement (
    trade_id VARCHAR(64) NOT NULL,
    cusip VARCHAR(20) NOT NULL,
    participant_id VARCHAR(20) NOT NULL,
    shares INT NOT NULL,
    settlement_amount DECIMAL(15, 2) NOT NULL,
    settlement_status VARCHAR(20) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    processed_flg VARCHAR(20) DEFAULT  NULL, 
    PRIMARY KEY (trade_id)
);

-- Insert Financial Trade Clearing Sample Data
INSERT INTO trade_settlement (trade_id, cusip, participant_id, shares, settlement_amount, settlement_status) 
VALUES 
('T-1001', '594918104', 'Financial-8821', 5000, 750000.00, 'PENDING'),
('T-1002', '037833100', 'Financial-4410', 12000, 2160000.00, 'SETTLED');
```

2. Postgres (Upsert Target for Real-Time Positions)
Connect to Postgres container:

```shell
podman exec -it postgresql psql -U postgres -d postgres
```

Create target schema and upsert table:

```SQL
CREATE SCHEMA IF NOT EXISTS financial;

CREATE TABLE financial.settlement_positions (
    trade_id VARCHAR(64) PRIMARY KEY,
    cusip VARCHAR(20) NOT NULL,
    participant_id VARCHAR(20) NOT NULL,
    shares INT NOT NULL,
    settlement_amount NUMERIC(15, 2) NOT NULL,
    settlement_status VARCHAR(20) NOT NULL,
    processed_at TIMESTAMP DEFAULT NOW()
);
```

3. MinIO / Iceberg Bucket Creation
Create the iceberg-bucket via MinIO client:

```shell
podman run --net data-orchestration --rm minio/mc \
  alias set myminio http://minio:9000 admin password123 && \
  podman run --net data-orchestration --rm minio/mc mb myminio/iceberg-bucket
```

Register Apps
Generate Register Script for jdbc Source (MySQL Reader):


Register JDBC Source:



Generate Register Script for postgres-upsert Processor:

```shell
echo app register --name postgres-upsert --type processor --bootVersion 3 --uri file://$PWD/applications/processors/postgres-upsert-processor/target/postgres-upsert-processor-0.0.1-SNAPSHOT.jar --metadataUri file://$PWD/applications/processors/postgres-upsert-processor/target/postgres-upsert-processor-0.0.1-SNAPSHOT-metadata.jar > runtime/scripts/postgres-upsert-processor.shell
cat runtime/scripts/postgres-upsert-processor.shell
```

Register Postgres Upsert Processor:

```shell
java -jar runtime/scdf/spring-cloud-dataflow-shell-2.11.5.jar --dataflow.uri=http://localhost:9393 --spring.shell.commandFile=runtime/scripts/postgres-upsert-processor.shell
```
Generate Register Script for iceberg Sink:

```shell
echo app register --name iceberg-s3-sink --type sink --bootVersion 3 --uri file://$PWD/applications/sinks/iceberg-s3-sink/target/iceberg-s3-sink-0.0.1-SNAPSHOT.jar --metadataUri file://$PWD/applications/sinks/iceberg-s3-sink/target/iceberg-s3-sink-0.0.1-SNAPSHOT-metadata.jar > runtime/scripts/iceberg-s3-sink-register.shell
cat runtime/scripts/iceberg-s3-sink-register.shell
```

Register Iceberg Sink:

```shell
java -jar runtime/scdf/spring-cloud-dataflow-shell-2.11.5.jar --dataflow.uri=http://localhost:9393 --spring.shell.commandFile=runtime/scripts/iceberg-s3-sink-register.shell
```

Open SCDF Dashboard:

```shell
open http://localhost:9393/dashboard/index.html#/apps
```

Configuration Files
Copy Financial pipeline configuration files:


--spring.datasource.username=root
--spring.datasource.password=CHANGEME
--spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
--spring.datasource.url="jdbc:mysql://localhost:3306/mysql"
jdbc:mariadb://localhost:3306/mysql

```shell
mysql-to-iceberg=jdbc --update="update trade_settlement set processed_flg = 'Y' where processed_flg IS NULL;" --query="SELECT *  FROM trade_settlement  WHERE processed_flg <> 'Y'     OR processed_flg IS NULL;" --password=root  --username=root --url="jdbc:mariadb://localhost:3306/mysql?allowPublicKeyRetrieval=true&useSSL=false" --spring.sql.init.platform=mysql  --fixed-delay=1000 | log
```

```shell
cp applications/sources/jdbc-source/src/main/resources/mysql-financial-source.yml /tmp
cp applications/processors/postgres-upsert-processor/src/main/resources/postgres-financial-upsert.yml /tmp
cp applications/sinks/iceberg-s3-sink/src/main/resources/iceberg-minio-sink.yml /tmp
```

In SCDF
Create Stream
```shell
financial-settlement-pipeline=jdbc-source | postgres-upsert | iceberg-s3-sink
```

Deploy Stream Configuration

```Properties
# Source: MySQL Financial Settlements
app.jdbc-source.jdbc.supplier.query="SELECT trade_id, cusip, participant_id, shares, settlement_amount, settlement_status FROM trade_settlement"
app.jdbc-source.jdbc.supplier.update="UPDATE trade_settlement SET settlement_status = 'PROCESSED' WHERE trade_id = :trade_id"
app.jdbc-source.spring.datasource.url="jdbc:mysql://localhost:3306/financial_clearing"
app.jdbc-source.spring.datasource.username=root
app.jdbc-source.spring.datasource.password=financialpass
app.jdbc-source.spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Processor: Postgres Upsert
app.postgres-upsert.spring.datasource.url="jdbc:postgresql://localhost:5432/financial_positions"
app.postgres-upsert.spring.datasource.username=postgres
app.postgres-upsert.spring.datasource.password=postgres
app.postgres-upsert.spring.datasource.driver-class-name=org.postgresql.Driver
app.postgres-upsert.upsert.query="INSERT INTO financial.settlement_positions (trade_id, cusip, participant_id, shares, settlement_amount, settlement_status) VALUES (:trade_id, :cusip, :participant_id, :shares, :settlement_amount, :settlement_status) ON CONFLICT (trade_id) DO UPDATE SET settlement_status = EXCLUDED.settlement_status, shares = EXCLUDED.shares, processed_at = NOW()"

# Sink: Apache Iceberg MinIO S3 Warehouse
app.iceberg-s3-sink.iceberg.catalog.type=hadoop
app.iceberg-s3-sink.iceberg.catalog.warehouse="s3a://iceberg-bucket/warehouse"
app.iceberg-s3-sink.iceberg.table=default_db.financial_settlements_history
app.iceberg-s3-sink.spring.hadoop.fs.s3a.endpoint="http://localhost:9000"
app.iceberg-s3-sink.spring.hadoop.fs.s3a.access.key=admin
app.iceberg-s3-sink.spring.hadoop.fs.s3a.secret.key=password123
app.iceberg-s3-sink.spring.hadoop.fs.s3a.path.style.access=true
```

Testing Data Pipeline Flow
1. Insert new Financial trade in MySQL
```SQL
INSERT INTO financial_clearing.trade_settlement (trade_id, cusip, participant_id, shares, settlement_amount, settlement_status) 
VALUES ('T-1003', '30231G102', 'Financial-9901', 8500, 1420000.00, 'PENDING');
```

2. Verify Upsert in Postgres
```SQL
SELECT * FROM financial.settlement_positions WHERE trade_id = 'T-1003';
```

3. Verify Lakehouse Record in Iceberg / MinIO

4. Check Iceberg metadata generated inside MinIO container:

```shell
podman run --net data-orchestration --rm minio/mc ls myminio/iceberg-bucket/warehouse/default_db/financial_settlements_history/metadata/
```

Tear Down
Stop Data Flow Server (Ctrl + C)

Stop Skipper (Ctrl + C)

Stop Services:

```shell
podman rm -f rabbitmq mysql-financial postgres-upsert minio
```