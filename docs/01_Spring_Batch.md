
```shell
podman network create data-orchestration
```

```shell
podman run --name postgresql --network data-orchestration --rm  -e POSTGRESQL_USERNAME=postgres -e ALLOW_EMPTY_PASSWORD=true -e POSTGRESQL_DATABASE=postgres -p 5432:5432 bitnami/postgresql:latest 
```

Start ValKey

```shell
podman run -it --rm \
  --name valkey \
  -p 6379:6379 \
  valkey/valkey:latest
```


## Running the accounts

```shell
java -jar applications/account-batch/target/account-batch-0.0.1-SNAPSHOT.jar --spring.profiles.active=postgres  --db.schema=cache_accounts --spring.data.redis.host="localhost" --spring.data.redis.port=6379 --batch.jdbc.url="jdbc:postgresql://localhost:5432/postgres"  --batch.jdbc.username=postgres --spring.sql.init.platform=postgres --batch.job.repository.create=true --spring.datasource.url="jdbc:postgresql://localhost:5432/postgres" --spring.datasource.username=postgres --batch.load.accounts=true --account.data.count=10000  --account.data.batch.size=50 
```


## Review Source Data in Postgres

1. Access Psql

```shell
podman exec -it postgresql psql -U postgres -d postgres
```

2. Select Source Data

```psql
select * from cache_accounts.account;
```

## Review Target Results in Postgres

1. Access Valkey

```shell
podman exec -it valkey valkey-cli
```
2. Look at all keys

Once inside the CLI:

```valkey-cli
keys *
```

3. Inspect a string value

If you know the key is a string:


```valkey-cli
HGETALL "spring.gemfire.showcase.account.domain.account.Account:1"
```


