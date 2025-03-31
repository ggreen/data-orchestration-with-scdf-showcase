# Prequisite

```shell
docker network create data-orchestration
```

- Run RabbitMQ (user/bitnami)
```shell
docker run --name rabbitmq  --rm -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:4.0.4 
```

```shell
docker run --name postgresql --network data-orchestration --rm  -e POSTGRESQL_USERNAME=postgres -e ALLOW_EMPTY_PASSWORD=true -e POSTGRESQL_DATABASE=postgres -p 5432:5432 bitnami/postgresql:latest 
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



```shell
docker run --name psql -it --rm \
--network data-orchestration \
    bitnami/postgresql:latest psql -h postgresql -U postgres
```


```sql

create schema customer;

create table customer.customers(
first_nm text NOT NULL,
last_nm text  NOT NULL,
email text NOT NULL,
phone text ,
address text NOT NULL,
city text ,
state text ,
zip text NOT NULL,
 PRIMARY KEY (email)
);

```
==================================

Register Apps

```properties
sink.postgres=file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/sinks/postgres-sink/target/postgres-sink-0.0.1-SNAPSHOT.jar
sink.postgres.metadata=file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/sinks/postgres-sink/target/postgres-sink-0.0.1-SNAPSHOT-metadata.jar
sink.postgres.bootVersion=3
processor.postgres-query=file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/processors/postgres-query-processor/target/postgres-query-processor-0.0.1-SNAPSHOT.jar
processor.postgres-query.metadata=file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/processors/postgres-query-processor/target/postgres-query-processor-0.0.1-SNAPSHOT-metadata.jar
processor.postgres-query.bootVersion=3

```

Data Flow

```shell
http-postgres-processor=http | postgres-query --sql="select :email as email,initcap(:firstname) as firstname,initcap(:lastname) as lastname,:phone as phone,:address as address,:city as city,:state as state,:zip as zip" | postgres --sql="insert into customer.customers(email,first_nm,last_nm,phone,address,city,state,zip) values (:email,:firstname,:lastname,:phone, :address,:city,:state,:zip) on CONFLICT (email) DO UPDATE SET first_nm = :firstname, last_nm = :lastname,  phone = :phone, address = :address, city = :city, state = :state, zip = :zip"
```


Deploy

```properties
app.http.path-pattern=customers
app.http.server.port=8091

app.postgres.spring.datasource.username=postgres
app.postgres.spring.datasource.url="jdbc:postgresql://localhost/postgres"
app.postgres.spring.datasource.driverClassName=org.postgresql.Driver

app.postgres-query.spring.datasource.username=postgres
app.postgres-query.spring.datasource.url="jdbc:postgresql://localhost/postgres"
app.postgres-query.spring.datasource.driverClassName=org.postgresql.Driver


```


```shell
curl -X 'POST' \
  'http://localhost:8091/customers' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "email" : "jmatthews@email",
  "firstname" : "jonny",
  "lastname" : "matthews",
  "phone" : "555-555-5555",
  "address" : "55 Straight St",
  "city" : "gold",
  "state": "ny",
  "zip": "55555"
}'
```


In psql

```sql
select * from customer.customers;

```