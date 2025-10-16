# Http Pull Source

Http Pull Source to publish HTTP payload to RabbitMQ.



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

```shell

```