# Prequisite

```shell
docker network create data-orchestration
```

- Run RabbitMQ (guest/guest)
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


--------

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

Reimport applications


Build application

```shell
mvn package
```

```shell
cp applications/sinks/postgres-sink/target/postgres-sink-0.0.1-SNAPSHOT.jar /tmp
cp applications/sinks/postgres-sink/target/postgres-sink-0.0.1-SNAPSHOT-metadata.jar /tmp
```


```shell
open http://localhost:9393/dashboard
```


```shell
sink.postgres=file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/sinks/postgres-sink/target/postgres-sink-0.0.1-SNAPSHOT.jar
sink.postgres.metadata=file:///Users/Projects/solutions/ai-ml/dev/ai-data-orchestration-with-scdf-showcase/applications/sinks/postgres-sink/target/postgres-sink-0.0.1-SNAPSHOT-metadata.jar
sink.postgres.bootVersion=3

```

```shell
customer-postgres-api=http --port=8090 --path-pattern=customers | postgres
```

Create Stream

Deploy the team


```properties

app.http.path-pattern=customers
app.http.server.port=8090
app.postgres.sql.consumer.sql="insert into customer.customers(email,first_nm,last_nm,phone,address,city,state,zip) values (:email,:firstName,:lastName,:phone, :address,:city,:state,:zip) on CONFLICT (email) DO UPDATE SET first_nm = :firstName, last_nm = :lastName,  phone = :phone, address = :address, city = :city, state = :state, zip = :zip"
app.postgres.spring.datasource.username=postgres
app.postgres.spring.datasource.url="jdbc:postgresql://localhost/postgres"
app.postgres.spring.datasource.driverClassName=org.postgresql.Driver
```


## Testing


```shell
curl -X 'POST' \
  'http://localhost:8090/customers' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "email" : "john@email",
  "firstName" : "John",
  "lastName" : "Smith",
  "phone" : "555-555-5555",
  "address" : "12 Straight St",
  "city" : "gold",
  "state": "ny",
  "zip": "55555"
}'
```


In psql 

```sql
select * from customer.customers;

```

