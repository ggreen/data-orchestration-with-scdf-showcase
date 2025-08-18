

```shell
http-api=http --port=8081 --path-pattern=customers | log
```


Create Team


Deploy the team


## Testing

Example 

```json
{
  "firstName" : "Josiah",
  "lastName" : "Imani",
  "email" : "email@email",
  "phone" : "555-555-5555",
  "address" : "12 Straight St",
  "city" : "gold",
  "zip": "55555"
}
```



```shell
curl -X 'POST' \
  'http://localhost:8081/customers' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "firstName" : "Josiah",
  "lastName" : "Imani",
  "email" : "email@email",
  "phone" : "555-555-5555",
  "address" : "12 Straight St",
  "city" : "gold",
  "state" "ny",
  "zip": "55555"
}'
```


Upper Case

```shell
customer-api-uppercase=http --port=8082 --path-pattern=customers/upper | transform --expression=payload.toUpperCase() | log
```


```shell
curl -X 'POST' \
  'http://localhost:8082/customers/upper' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "firstName" : "Josiah",
  "lastName" : "Imani",
  "email" : "email@email",
  "phone" : "555-555-5555",
  "address" : "12 Straight St",
  "city" : "gold",
  "state" "ny",
  "zip": "55555"
}'
```
