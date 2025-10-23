# Rabbit Stream Sink

The Rabbit Stream Sink application is a Spring Cloud Stream application that reads messages from an input channel and 
publishes them to a RabbitMQ stream. It is built using Spring Boot and Spring Cloud Stream, leveraging the RabbitMQ Stream Binder 
for integration with RabbitMQ streams.

## Getting Started

```shell
java -jar applications/sinks/rabbit-stream-sink/target/rabbit-stream-sink-0.0.1.jar --rabbit.stream.sink.streamName=output   
```

## Configurations

The following configurations are available for the Rabbit Stream Sink application:

| Property                                       | Notes                                                                                                                   |
|------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| rabbit.stream.sink.streamName                  | The name of the RabbitMQ stream to publish                                                                              |
| spring.rabbitmq.username                       | RabbitMQ username. (default guest)                                                                                      |
| spring.rabbitmq.password                       | RabbitMQ password. (default guest)                                                                                      |
| spring.rabbitmq.virtual-host                   | RabbitMQ virtual host. (default /)                                                                                      |
| spring.rabbitmq.addresses                      | List of addresses to which the client should connect. When set, the host and port are ignored.                          |
| spring.rabbitmq.host                           | RabbitMQ host. Ignored if an address is set. (default localhost)                                                        |
| spring.rabbitmq.stream.host                    | Host of a RabbitMQ instance with the Stream plugin enabled.                                                             |
| spring.rabbitmq.stream.name                    | Name of the stream.                                                                                                     |
| spring.rabbitmq.stream.username                | Login username to authenticate to the broker. When not set spring.rabbitmq.username is used.                            |
| spring.rabbitmq.stream.password                | Login password to authenticate to the broker. When not set spring.rabbitmq.password is used.                            |
| spring.rabbitmq.stream.virtual-host            | Virtual host of a RabbitMQ instance with the Stream plugin enabled. When not set, spring.rabbitmq.virtual-host is used. | 
| spring.rabbitmq.stream.port                    | Login password to authenticate to the broker. When not set spring.rabbitmq.password is used.                            |
| spring.cloud.stream.bindings.input.destination | The name of the RabbitMQ inbound queue                                                                                  |


## Enable RabbitMQ Stream

Note must enable [RabbitMQ Streams](https://www.rabbitmq.com/docs/streams)

Use the following to enable on Cloud Foundry

```shell
cf create-service retail-rabbitmq -c '{ "plugins": { "rabbitmq_stream": true, "rabbitmq_stream_management": true } }'
```

-----------------------

# Spring Cloud Data Flow

Setup on Data Flow

```shell
open http://localhost:9393/dashboard/index.html#/apps/add
```

Use the following properties to register the application in Data Flow

```shell
sink.stream-sink=maven://com.github.ggreen:rabbit-stream-sink:0.0.1
sink.stream-sink.bootVersion=3
```

Example Data Flow Stream

```scdf
http-stream=http --port=8555 | stream-sink --rabbit.stream.sink.streamName=greenplum
```


----------------

# Testing SCDF Deployments

Test Stream for testing locally

```shell
curl -X POST http://localhost:8555 \
-H "Content-Type: application/json" \
-d '{"key":"value","anotherKey":123}'
```


Posting Randomly Generated Data 

```shell
export STREAM_URL=http://localhost:8555
for i in {1..20}
do
  uuid=$(uuidgen)
  num=$RANDOM
  firstName="John$num"
  lastName="Doe$num"
  email="john.doe$num@example.com"
  phone="555-01$(printf "%03d" $num)"
  json="{\"id\":\"$uuid\",\"value\":$num,\"firstName\":\"$firstName\",\"lastName\":\"$lastName\",\"email\":\"$email\",\"phone\":\"$phone\"}"
  curl -X POST $STREAM_URL \
    -H "Content-Type: application/json" \
    -d "$json"
  echo "$json"
done
```