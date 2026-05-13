

Application Properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=
spring.datasource.driver-class-name=org.postgresql.Driver

db.url=jdbc:postgresql://localhost:5432/postgres
db.username=postgres
db.password=
db.driverClassName=org.postgresql.Driver
db.sql=call accounts.reset_daily_counters();

logging.level.org.springframework.jdbc.core=DEBUG
```


Open Psql


```shell
podman exec -it postgresql psql -U postgres
```

```sql
CREATE SCHEMA IF NOT EXISTS accounts;
```

```sql
CREATE TABLE accounts.account_limits (
    account_id TEXT PRIMARY KEY,
    daily_limit_used INTEGER NOT NULL
);
```

Create 

```sql
CREATE OR REPLACE PROCEDURE accounts.reset_daily_counters()
LANGUAGE plpgsql
AS $$
BEGIN
    -- This runs exactly the same way every time it is called
    UPDATE accounts.account_limits 
    SET daily_limit_used = 0;

    RAISE NOTICE 'All user counters have been reset.';
END;
$$;
```


Insert data

```sql
INSERT INTO accounts.account_limits (account_id, daily_limit_used)
VALUES 
    ('ACC001', 50),
    ('ACC002', 120),
    ('ACC003', 0),
    ('ACC004', 450),
    ('ACC005', 10),
    ('ACC006', 85),
    ('ACC007', 300),
    ('ACC008', 25),
    ('ACC009', 0),
    ('ACC010', 500);
```

Run application

```sql
select * from accounts.account_limits;
```



```shell script
docker build --file=Dockerfile --tag=cloudnativedata/sql-task:0.0.1 --platform linux/amd64,linux/arm64 --rm=true .
docker ps
docker login
docker push cloudnativedata/sql-task:0.0.1 
```


In docker (In Kubernetes)

```properties
task.sql-task=docker://cloudnativedata/sql-task:0.0.1
```

Maven (on Tanzu Platform)

```properties
task.sql-task=maven://com.github.ggreen:sql-task:0.0.1
```