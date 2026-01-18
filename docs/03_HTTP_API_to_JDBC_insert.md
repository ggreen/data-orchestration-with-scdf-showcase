# HTTP API to JDBC Stream

![http_api_to_jdbc.png](img/http_api_to_jdbc.png)


Create podman network (if it does not exist)

```shell
podman network create data-orchestration
```

## Start RabbitMQ

## Postgres

Start if not running

```shell
./deployment/local/podman/postgres/start.sh  
```

- Run RabbitMQ (guest/guest) if not running
```shell
deployment/local/podman/rabbit/start.sh 
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


## PSQL

```shell
podman exec -it postgresql psql -U postgres -d postgres
```


Setup tables

```sql

create schema IF NOT EXISTS customer;

create table customer.customers(
email text NOT NULL,
first_nm text NOT NULL,
last_nm text  NOT NULL,
phone text ,
address text NOT NULL,
city text ,
state text ,
zip text NOT NULL,
 PRIMARY KEY (email)
);

```

Open SCDF Dashboard


```shell
open http://localhost:9393/dashboard/index.html#/apps
```

Import RabbitMQ applications from Maven

- Click Add Applications

![Import-RabbitMQ-applications.png](img/Import-RabbitMQ-applications.png)


Open Streams

```shell
open http://localhost:9393/dashboard/index.html#/streams/list
```

Click Create Stream

- Paste Definition

```shell
customer-database-api=http --port=8084 --path-pattern=customers | jdbc --columns=first_nm:firstName,last_nm:lastName,email,phone,address,city,state,zip --table-name=customer.customers
```


Create Stream

Deploy the team

![deploy_stream.png](img/deploy_stream.png)


Click Deploy -> Free Text

![Deploy-Free-Text.png](img/Deploy-Free-Text.png)


Paste the following properties

```properties
app.http.path-pattern=customers
app.http.server.port=8084
app.jdbc.consumer.columns=first_nm:firstName,last_nm:lastName,email,phone,address,city,state,zip
app.jdbc.consumer.table-name=customer.customers
app.jdbc.spring.datasource.username=postgres
app.jdbc.spring.datasource.url="jdbc:postgresql://localhost/postgres"
app.jdbc.spring.datasource.driverClassName=org.postgresql.Driver
```


## Testing


Submit Customer Data

```shell
curl -X 'POST' \
  'http://localhost:8084/customers' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "firstName" : "Josiah",
  "lastName" : "Imani",
  "email" : "email@email",
  "phone" : "555-555-5555",
  "address" : "12 Straight St",
  "city" : "gold",
  "state": "ny",
  "zip": "55555"
}'
```


Select Data in Psql

```sql
select * from customer.customers;
```

Post Customer

```shell
curl -X 'POST' \
  'http://localhost:8084/customers' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "firstName" : "Jill",
  "lastName" : "Smith",
  "email" : "jsmith@email",
  "phone" : "155-555-5555",
  "address" : "2 Straight St",
  "city" : "gold",
  "state": "ny",
  "zip": "55555"
}'
```

Select Results in psql
```sql
select * from customer.customers;
```

Post Customer

```shell
curl -X 'POST' \
  'http://localhost:8084/customers' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "firstName" : "Jack",
  "lastName" : "Smith",
  "email" : "jacksmith@email",
  "phone" : "255-555-5555",
  "address" : "255 Straight St",
  "city" : "gold",
  "state": "ny",
  "zip": "55555"
}'
```

Select Data in PSQL

```sql
select * from customer.customers;

```
Change Jill Smith Phone (ERROR)

```shell
curl -X 'POST' \
  'http://localhost:8084/customers' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "firstName" : "Jill",
  "lastName" : "Smith",
  "email" : "jsmith@email",
  "phone" : "222-555-5555",
  "address" : "2 Straight St",
  "city" : "gold",
  "state": "ny",
  "zip": "55555"
}'
```

Phone number not changed
```sql
select * from customer.customers;

```


See JDBC Logs in SCDF Dashboard

```text
ERROR: duplicate key value violates unique constraint "customers_pkey"
  Detail: Key (email)=(jsmith@email) already exists.
```

Type in psql
```shell
exit
```


-----------------------
# Tear Down (optional)

- Stop Data Flow Server (Control C)
- Stop SKipper (Control C)

Stop Services

```shell
podman rm -f rabbitmq postgresql
```