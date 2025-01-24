

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

```shell
customer-database-api=http --port=8084 --path-pattern=customers | jdbc --columns=first_nm:firstName,last_nm:lastName,email,phone,address,city,state,zip --table-name=customer.customers
```


Create Stream

Deploy the team


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



```sql
select * from customer.customers;

```


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
```sql
select * from customer.customers;

```
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

```sql
select * from customer.customers;

```

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

```sql
select * from customer.customers;

```
Change Jill Smith Phone

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


Logs

```text
ERROR: duplicate key value violates unique constraint "customers_pkey"
  Detail: Key (email)=(jsmith@email) already exists.
```


