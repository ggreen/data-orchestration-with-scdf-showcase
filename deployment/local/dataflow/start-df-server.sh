export ROOT_DIR=$PWD
export SPRING_APPLICATION_JSON='{"spring.cloud.stream.binders.rabbitBinder.environment.spring.rabbitmq.username":"guest","spring.cloud.stream.binders.rabbitBinder.environment.spring.rabbitmq.password":"guest","spring.rabbitmq.username":"guest","spring.rabbitmq.password":"guest","spring.cloud.dataflow.applicationProperties.stream.spring.rabbitmq.username" :"guest","spring.cloud.dataflow.applicationProperties.stream.spring.rabbitmq.password" :"guest"}'

java -jar runtime/scdf/spring-cloud-dataflow-server-2.11.5.jar