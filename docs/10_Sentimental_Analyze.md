Run Rabbit

```shell
docker run --name rabbitmq  --rm -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:4.0.4 
```

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
    bitnami/postgresql:latest psql -h postgresml  -U postgres -d postgresml
```

0.9959648847579956

```shell
create  schema  if not exists customer ;

create table customer.feedback(
    feed_id text NOT NULL,
    email text NOT NULL,
    user_feedback text NOT NULL,
    summary text NOT NULL,
    feedback_dt timestamp NOT NULL DEFAULT NOW(),
    sentiment smallint NOT NULL,
    score numeric NOT NULL,
 PRIMARY KEY (feed_id)
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

::json->>'summary_text'

select pg_typeof(results::json)

```sql
SELECT pg_typeof(pgml.transform( task => '{ "task": "summarization", "model": "google/pegasus-xsum"}'::JSONB, inputs => array[ 'Paris is the capital and most populous city of France, with an estimated population of 2,175,601 residents as of 2018, in an area of more than 105 square kilometres (41 square miles). The City of Paris is the centre and seat of government of the region and province of Île-de-France, or Paris Region, which has an estimated population of 12,174,880, or about 18 percent of the population of France as of 2017.'])::json->0->>'summary_text') as summary_text;


SELECT pgml.transform( task => '{ "task": "summarization", "model": "google/pegasus-xsum"}'::JSONB, inputs => array[ 'Paris is the capital and most populous city of France, with an estimated population of 2,175,601 residents as of 2018, in an area of more than 105 square kilometres (41 square miles). The City of Paris is the centre and seat of government of the region and province of Île-de-France, or Paris Region, which has an estimated population of 12,174,880, or about 18 percent of the population of France as of 2017.'])::json->0->>'summary_text' as summary_text;
```

```shell
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
        'Why is the wait SO LONG!' ]
        ) as positivity) text_classification;
```

```shell
http-text-sentiment=http | summarize: postgres-query | sentiment: postgres-query | postgres
```


Deploy

```properties
app.http.path-pattern=feedback
app.http.server.port=8094

app.summarize.spring.datasource.username=postgres
app.summarize.spring.datasource.url="jdbc:postgresql://localhost:6432/postgresml"
app.summarize.spring.datasource.driverClassName=org.postgresql.Driver
app.summarize.spring.config.import=optional:file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/processors/postgres-query-processor/src/main/resources/text-summarization.yml
app.summarize.spring.datasource.hikari.max-lifetime=600000

app.sentiment.spring.datasource.username=postgres
app.sentiment.spring.datasource.url="jdbc:postgresql://localhost:6432/postgresml"
app.sentiment.spring.datasource.driverClassName=org.postgresql.Driver
app.sentiment.spring.config.import=optional:file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/processors/postgres-query-processor/src/main/resources/sentiment-analysis.yml
app.sentiment.spring.datasource.hikari.max-lifetime=600000


app.postgres.spring.datasource.username=postgres
app.postgres.spring.datasource.url="jdbc:postgresql://localhost:6432/postgresml"
app.postgres.spring.config.import=optional:file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/sinks/postgres-sink/src/main/resources/postgres-sentiment-analysis.yml
app.postgres.spring.datasource.driverClassName=org.postgresql.Driver
app.postgres.spring.datasource.hikari.max-lifetime=600000
```


```shell
curl -X 'POST' \
  'http://localhost:8094/feedback' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" : "F001",
  "email" : "jmatthews@email",
  "feedback" : "Hello my name is John Smith. I am long time customer. It seems that every time I call the help desk there is a very long wait. Then when I following get someone on the line, I have the repeat to repeat the process of the provide the details. This is very disappointing."
}'
```


In psql

```sql
select * from customer.feedback;

```