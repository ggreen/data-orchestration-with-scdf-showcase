# Rabbit Stream Sink

The Rabbit Stream Sink application is a Spring Cloud Stream application that reads messages from an input channel and publishes them to a RabbitMQ stream. It is built using Spring Boot and Spring Cloud Stream, leveraging the RabbitMQ Stream Binder for integration with RabbitMQ streams.

## Getting Started

```shell
java -jar applications/sinks/rabbit-stream-sink/target/rabbit-stream-sink-0.0.1-SNAPSHOT.jar --rabbit.stream.sink.streamName=output   
```

## Configurations

The following configurations are available for the Rabbit Stream Sink application:

| Property                                       | Notes                                                                                          |
|------------------------------------------------|------------------------------------------------------------------------------------------------|
| rabbit.stream.sink.streamName                  | The name of the RabbitMQ stream to publish                                                     |
| spring.rabbitmq.username                       | RabbitMQ username. (default guest)                                                             |
| spring.rabbitmq.password                       | RabbitMQ password. (default guest)                                                             |
| spring.rabbitmq.virtual-host                   | RabbitMQ virtual host. (default /)                                                             |
| spring.rabbitmq.addresses                      | List of addresses to which the client should connect. When set, the host and port are ignored. |
| spring.rabbitmq.host                           | RabbitMQ host. Ignored if an address is set. (default localhost)                               |
| spring.cloud.stream.bindings.input.destination | The name of the RabbitMQ inbound queue                                                         |




# Spring Cloud Data Flow


```shell
open http://localhost:9393/dashboard/index.html#/apps/add
```

Add properties 

````shell
echo sink.stream-sink="file://$PWD/applications/sinks/rabbit-stream-sink/target/rabbit-stream-sink-0.0.1-SNAPSHOT.jar"
echo sink.stream-sink.bootVersion=3
echo sink.stream-sink.metadata="file://$PWD/applications/sinks/rabbit-stream-sink/target/rabbit-stream-sink-0.0.1-SNAPSHOT-metadata.jar"
````

Example Stream

```scdf
http-stream=http --port=8555 | stream-sink --rabbit.stream.sink.streamName=greenplum
```


Test Stream

```shell
curl -X POST http://localhost:8555 \
-H "Content-Type: application/json" \
-d '{"key":"value","anotherKey":123}'
```
