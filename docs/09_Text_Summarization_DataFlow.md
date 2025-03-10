

```shell
docker run --name psql -it --rm \
--network data-orchestration \
    bitnami/postgresql:latest psql -h postgresql -U postgres
```


```sql

create schema customer;

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
