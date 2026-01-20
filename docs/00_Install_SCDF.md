# Install SCDF

Create podman network (if it does not exist)

```shell
podman network create data-orchestration
```


# Start SCDF


Download SCDF Jars (optional first time only)
- SCDF Server
- Skipper
- Shell

```shell
mkdir -p runtime/scdf
wget  --directory-prefix=runtime/scdf https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-dataflow-server/2.11.5/spring-cloud-dataflow-server-2.11.5.jar
wget --directory-prefix=runtime/scdf https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-skipper-server/2.11.5/spring-cloud-skipper-server-2.11.5.jar
wget --directory-prefix=runtime/scdf https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-dataflow-shell/2.11.5/spring-cloud-dataflow-shell-2.11.5.jar
```
--------------------------

# Start RabbitMQ

- Clone the git repo
```
git clone https://github.com/ggreen/data-orchestration-with-scdf-showcase.git
cd data-orchestration-with-scdf-showcase
```

- Run RabbitMQ (guest/guest)
```shell
deployment/local/podman/rabbit/start.sh
```

Start Skipper
```shell
./deployment/local/dataflow/start-skipper.sh
```


Start Data Flow Server
```shell
./deployment/local/dataflow/start-df-server.sh
```


Open Dashboard

```shell
open http://localhost:9393/dashboard
```


Import Application

    Click Applications -> Add Applications -> import application starters from dataflow.spring.io -> Stream application starters for RabbitMQ/Maven

![Import-RabbitMQ-applications.png](img/Import-RabbitMQ-applications.png)


Access CLI

```shell
deployment/local/dataflow/shell.sh
```

List apps

```shell
app list
```

Type Exit close

```shell
exit
```
