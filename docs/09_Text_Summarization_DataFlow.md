Run Postgres

```shell
```shell
docker run --name postgresql --network data-orchestration --rm  -e POSTGRESQL_USERNAME=postgres -e ALLOW_EMPTY_PASSWORD=true -e POSTGRESQL_DATABASE=postgres -p 5432:5432 bitnami/postgresql:latest 
```

Run PostgresML

```shell
docker run --rm --name postgresml \
    -it \
    --network data-orchestration  \
    -v postgresml_data:/var/lib/postgresql \
    -p 6432:5432 \
    -p 8000:8000 \
    ghcr.io/postgresml/postgresml:2.10.0 \
    sudo -u postgresml psql -d postgresml
```


```shell
docker run --name psql -it --rm \
--network data-orchestration \
    bitnami/postgresql:latest psql -h postgresml  -U postgres
```


```sql

create schema customer;

create table customer.feedback(
customer_id text NOT NULL,
user_feedback text NOT NULL,
summary text NOT NULL,
 PRIMARY KEY (customer_id)
);
```


Start Skipper
```shell
export ROOT_DIR=$PWD
export SPRING_APPLICATION_JSON='{"spring.datasource.username" : "postgres","spring.datasource.url": "jdbc:postgresql://localhost/postgres"}'
java -jar runtime/scdf/spring-cloud-skipper-server-2.11.5.jar
```


Start Data Flow Server
```shell
export ROOT_DIR=$PWD
export SPRING_APPLICATION_JSON='{"spring.cloud.stream.binders.rabbitBinder.environment.spring.rabbitmq.username":"user","spring.cloud.stream.binders.rabbitBinder.environment.spring.rabbitmq.password":"bitnami","spring.rabbitmq.username":"user","spring.rabbitmq.password":"bitnami","spring.cloud.dataflow.applicationProperties.stream.spring.rabbitmq.username" :"user","spring.cloud.dataflow.applicationProperties.stream.spring.rabbitmq.password" :"bitnami", "spring.datasource.username" : "postgres","spring.datasource.url": "jdbc:postgresql://localhost/postgres","spring.datasource.driverClassName": "org.postgresql.Driver"}'

java -jar runtime/scdf/spring-cloud-dataflow-server-2.11.5.jar
```

---------------------------

Register Apps


```shell
open http://localhost:9393/dashboard
```

```properties
sink.postgres=file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/sinks/postgres-sink/target/postgres-sink-0.0.1-SNAPSHOT.jar
sink.postgres.metadata=file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/sinks/postgres-sink/target/postgres-sink-0.0.1-SNAPSHOT-metadata.jar
sink.postgres.bootVersion=3
processor.postgres-query=file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/processors/postgres-query-processor/target/postgres-query-processor-0.0.1-SNAPSHOT.jar
processor.postgres-query.metadata=file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/processors/postgres-query-processor/target/postgres-query-processor-0.0.1-SNAPSHOT-metadata.jar
processor.postgres-query.bootVersion=3
```


```sql
SELECT pgml.transform( task => '{ "task": "summarization", "model": "google/pegasus-xsum"}'::JSONB, inputs => array[ 'Paris is the capital and most populous city of France, with an estimated population of 2,175,601 residents as of 2018, in an area of more than 105 square kilometres (41 square miles). The City of Paris is the centre and seat of government of the region and province of Île-de-France, or Paris Region, which has an estimated population of 12,174,880, or about 18 percent of the population of France as of 2017.']);
```


```shell
http-text-summary=http | postgres-query | log
```


Deploy

```properties
app.http.path-pattern=feedback
app.http.server.port=8093

app.postgres.spring.datasource.username=postgres
app.postgres.spring.datasource.url="jdbc:postgresql://localhost/postgresml"
app.postgres.spring.datasource.driverClassName=org.postgresql.Driver

app.postgres-query.spring.datasource.username=postgres
app.postgres-query.spring.datasource.url="jdbc:postgresql://localhost:6432/postgresml"
app.postgres-query.spring.datasource.driverClassName=org.postgresql.Driver
app.postgres-query.spring.config.import=optional:file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/processors/postgres-query-processor/src/main/resources/text-summarization.yml
```


```shell
curl -X 'POST' \
  'http://localhost:8093/feedback' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" : "jmatthews@email",
  "feedback" : "Paris is the capital and most populous city of France, with an estimated population of 2,175,601 residents as of 2018, in an area of more than 105 square kilometres (41 square miles). The City of Paris is the centre and seat of government of the region and province of Île-de-France, or Paris Region, which has an estimated population of 12,174,880, or about 18 percent of the population of France as of 2017.",
  "zip": "55555"
}'
```


In psql

```sql
select * from customer.customers;

```