# Intro Streaming

![intro-streaming-http-log.png](img/intro-streaming-http-log.png)


## HTTP Log Stream Pipeline


Open Dashboard

```shell
open http://localhost:9393/dashboard/index.html#/streams/list
```


- Click Create Stream(s)
- Paste Definition

```scdf
http-api=http --port=8081 --path-pattern=customers | log
```


- Create Stream 
- Deploy the Stream

![deploy_stream.png](img/deploy_stream.png)


## Testing

Example JJSON

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


Post to HTTP source

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

View Logs output in dashboard

-------------
## Upper Case Stream

 ![http-transform-log.png](img/http-transform-log.png)

Open Dashboard

```shell
open http://localhost:9393/dashboard/index.html#/streams/list
```

- Click Create Stream(s)
- Paste Definition


```shell
customer-api-uppercase=http --port=8082 --path-pattern=customers/upper | transform --expression=payload.toUpperCase() | log
```

- Click Create Stream

![create_stream_http_transform.png](img/create_stream_http_transform.png)


- Deploy Stream
- 
![deploy_stream.png](img/deploy_stream.png)

### Testing Stream


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


View Logs output in dashboard