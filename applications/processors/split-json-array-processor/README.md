# Spring Cloud Data Flow

Setup on Data Flow

```shell
open http://localhost:9393/dashboard/index.html#/apps/add
```

Use the following properties to register the application in Data Flow

```shell
echo processor.split-json-array=file://$PWD/applications/processors/split-json-array-processor/target/split-json-array-processor-0.0.1-SNAPSHOT.jar
echo processor.split-json-array.bootVersion=3
```


```shell
processor.stream-sink=maven://com.github.ggreen:rabbit-stream-sink:0.0.1
processor.stream-sink.bootVersion=3
```
