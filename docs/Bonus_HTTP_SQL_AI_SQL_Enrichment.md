# HTTP to SQL AI to Postgres

![http_to_sql_ai_postgres.png](img/http_to_sql_ai_postgres.png)


# Building Project

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

- Run RabbitMQ (guest/guest) if not running
```shell
deployment/local/podman/rabbit/start.sh 
```
## Postgres

Start if not running

```shell
./deployment/local/podman/postgres/start.sh  
```

# Start SCDF


Start Skipper (if not running)
```shell
deployment/local/dataflow/start-skipper.sh
```


Start Data Flow Server (if not running)
```shell
deployment/local/dataflow/start-df-server.sh
```

Run PostgresML

```shell
podman run --rm --name postgresml \
    -it \
    --network data-orchestration  \
    --cap-add=AUDIT_WRITE \
    -v postgresml_data:/var/lib/postgresql \
    -p 6432:5432 \
    -p 8000:8000 \
    ghcr.io/postgresml/postgresml:2.10.0 \
    sudo -u postgresml psql -d postgresml
```


```shell
podman run --name psql -it --rm \
--network data-orchestration \
    bitnami/postgresql:latest psql -h postgresml  -U postgres -d postgresml
```

In PostgresML

```shell
drop schema customer cascade;
create  schema customer ;

create table customer.feedback(
    feed_id text NOT NULL,
    email text NOT NULL,
    user_feedback text NOT NULL,
    feedback_dt timestamp NOT NULL DEFAULT NOW(),
    sentiment smallint NOT NULL,
    score numeric NOT NULL,
 PRIMARY KEY (feed_id)
);
```



---------------------------

Register Apps

Generate Register Script

```shell
mkdir -p runtime/scripts
echo app register --name postgres --type sink --bootVersion 3 --uri file://$PWD/applications/sinks/postgres-sink/target/postgres-sink-0.0.1-SNAPSHOT.jar --metadataUri file://$PWD/applications/sinks/postgres-sink/target/postgres-sink-0.0.1-SNAPSHOT-metadata.jar > runtime/scripts/postgres-sink-register.shell
cat runtime/scripts/postgres-sink-register.shell
```

Register Sink (if not registered)

```shell
java -jar runtime/scdf/spring-cloud-dataflow-shell-2.11.5.jar --dataflow.uri=http://localhost:9393 --spring.shell.commandFile=runtime/scripts/postgres-sink-register.shell
````


Generate Register Script

```shell
mkdir -p runtime/scripts
echo app register --name postgres-query --type processor --bootVersion 3 --uri file://$PWD/applications/processors/postgres-query-processor/target/postgres-query-processor-0.0.1-SNAPSHOT.jar  --metadataUri file://$PWD/applications/processors/postgres-query-processor/target/postgres-query-processor-0.0.1-SNAPSHOT-metadata.jar > runtime/scripts/postgres-query-processor.shell
cat runtime/scripts/postgres-query-processor.shell
```


Register App

```shell
java -jar runtime/scdf/spring-cloud-dataflow-shell-2.11.5.jar --dataflow.uri=http://localhost:9393 --spring.shell.commandFile=runtime/scripts/postgres-query-processor.shell
````
Open SCDF Dashboard


```shell
open http://localhost:9393/dashboard/index.html#/apps
```

Import RabbitMQ applications from Maven

- Click Add Applications

![Import-RabbitMQ-applications.png](img/Import-RabbitMQ-applications.png)


Test SQL in PostgresML

```psql
SELECT
        positivity::json->0->>'label' as label,
        positivity::json->0->>'score' as score,
        (CASE
        WHEN positivity::json->0->>'label' = 'NEGATIVE' THEN -1
        WHEN positivity::json->0->>'label' = 'POSITIVE' THEN 1
        ELSE
        0
        END) as sentiment
        from (SELECT pgml.transform(
        task   => 'text-classification',
        inputs => ARRAY[
        'I LOVE DATAFLOW!' ]
        ) as positivity) text_classification;
```


```shell
cp applications/processors/postgres-query-processor/src/main/resources/sentiment-analysis.yml /tmp
cp applications/sinks/postgres-sink/src/main/resources/postgres-sentiment-analysis.yml /tmp
```


In SCDF

Create Stream


```shell
http-text-sentiment=http --path-pattern=feedback | sentiment: postgres-query | postgres
```



Deploy

```properties
app.http.path-pattern=feedback
app.http.server.port=8094
app.sentiment.spring.datasource.username=postgres
app.sentiment.spring.datasource.url="jdbc:postgresql://localhost:6432/postgresml"
app.sentiment.spring.datasource.driverClassName=org.postgresql.Driver
app.sentiment.spring.config.import=optional:file:///tmp/sentiment-analysis.yml
app.sentiment.spring.datasource.hikari.max-lifetime=600000
app.postgres.spring.datasource.username=postgres
app.postgres.spring.datasource.url="jdbc:postgresql://localhost:6432/postgresml"
app.postgres.spring.config.import=optional:file:///tmp/postgres-sentiment-analysis.yml
app.postgres.spring.datasource.driverClassName=org.postgresql.Driver
app.postgres.spring.datasource.hikari.max-lifetime=600000
```


Testing with JSON

```json
    {
      "id" : "F001",
      "email" : "arul@email",
      "feedback" : "I LOVE DATA FLOW."
    }
```

```shell
curl -X 'POST' \
  'http://localhost:8094/feedback' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" : "F001",
  "email" : "arul@email",
  "feedback" : "I LOVE DATA FLOW."
}'
```


In PostgresML

```sql
select * from customer.feedback;
```

-----------------------
# Tear Down

- Stop Data Flow Server (Control C)
- Stop SKipper (Control C)

Stop Services

```shell
podman rm -f rabbitmq postgresml postgresql valkey
```