# Http Pull Source

Http Pull Source to publish HTTP payload to RabbitMQ.



Use the following properties to register the application in Data Flow

```shell
source.http-pull-source=maven://com.github.ggreen:http-pull-source:0.0.1
source.http-pull-source.bootVersion=3
```



Generate Register Script

```shell
mkdir -p runtime/scripts
echo app register --name http-pull-source --type source --bootVersion 3 --uri file://$PWD/applications/source/http-pull-source/target/http-pull-source-0.0.1.jar --metadata-uri file://$PWD/applications/source/http-pull-source/target/http-pull-source-0.0.1-metadata.jar> runtime/scripts/http-pull-source-register.shell
cat runtime/scripts/http-pull-source-register.shell
```


Register Sink

```shell
java -jar runtime/scdf/spring-cloud-dataflow-shell-2.11.5.jar --dataflow.uri=http://localhost:9393 --spring.shell.commandFile=runtime/scripts/http-pull-source-register.shell
````

Data Flow Streams

Configurations


The following configurations are available for the HTTP Pull Source application:

| Property                                    | Notes                                     |
|---------------------------------------------|-------------------------------------------|
| http.pull.source.authenticateUrlSecret      | The authenticate Url Secret               |
| http.pull.source.authenticateUrlParamSecret | The authenticate Url Param (ex: the user) |
| http.pull.source.pullUrlSecret              | The URL to pull HTTP data                 |
| spring.rabbitmq.username                       | RabbitMQ username. (default guest)                                                                                      |
| spring.rabbitmq.password                       | RabbitMQ password. (default guest)                                                                                      |
| spring.rabbitmq.virtual-host                   | RabbitMQ virtual host. (default /)                                                                                      |
| spring.rabbitmq.addresses                      | List of addresses to which the client should connect. When set, the host and port are ignored.                          |
| spring.rabbitmq.host                           | RabbitMQ host. Ignored if an address is set. (default localhost)                                                        |



Also see https://docs.spring.io/spring-boot/appendix/application-properties/index.html#application-properties.integration.spring.rabbitmq.addresses
