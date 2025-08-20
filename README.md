
# Spring Cloud Data Flow (SCDF) Data Orchestration Show Case
## Overview

[Spring Cloud Data Flow (SCDF)](https://spring.io/projects/spring-cloud-dataflow) is a **data integration and orchestration service** for composing, deploying, and managing **data pipelines**.

It provides:
-
- **Streaming pipelines** for event-driven use cases (real-time ETL, messaging, analytics).
- **Task pipelines** for batch or scheduled workloads (machine learning jobs, database migrations, reporting).
- **Application orchestration** across multiple runtimes: Cloud Foundry, Kubernetes, or Local.
- **Scalability** with partitioning, scaling, and monitoring support.

A pipeline in SCDF is built from **Spring Cloud Stream** and **Spring Cloud Task** applications, typically composed of:
- **Source** – Ingests data (e.g., from Kafka, RabbitMQ, HTTP, File, JDBC).
- **Processor** – Transforms or enriches data.
- **Sink** – Writes data to a target system (e.g., database, messaging system, file, analytics store).

---


# Applications

| Application                                                                  | Notes                                  |
|------------------------------------------------------------------------------|----------------------------------------|
| [account-batch](applications/batching/db_to_caching/account-batch)           | Spring Batch/Task Example              |
| [postgres-query-processor](applications/processors/postgres-query-processor) | Postgres Based SQL Streaming Processor |
| [postgres-sink](applications/sinks/postgres-sink)                            | Postgres Based SQL Streaming Sink      |


# Labs

See [Hands On Labs](docs)




