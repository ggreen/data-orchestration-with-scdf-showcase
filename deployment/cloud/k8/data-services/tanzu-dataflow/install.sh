  kubectl create namespace tanzu-data
  kubectl config set-context --current --namespace=tanzu-data


helm registry login tanzu-dataflow.packages.broadcom.com \
  --username=$BROADCOM_USERNAME \
  --password=${DATAFLOW_SUPPORT_TOKEN}


  kubectl create secret docker-registry tanzu-dataflow-registry-secret \
    --docker-server=registry.packages.broadcom.com \
    --docker-username=$BROADCOM_USERNAME \
    --docker-password="${DATAFLOW_SUPPORT_TOKEN}"

# Skipper Server database secret
export db_username=`kubectl get secret postgres-db-db-secret -o jsonpath='{.data.username}' | base64 --decode`
export db_password=`kubectl get secret postgres-db-db-secret -o jsonpath='{.data.password}' | base64 --decode`

export rabbit_username="$(kubectl get secret rabbitmq-default-user -o jsonpath='{.data.username}' | base64 --decode)"
export rabbit_password="$(kubectl get secret rabbitmq-default-user -o jsonpath='{.data.password}' | base64 --decode)"
echo RabbitQ - user $rabbit_username password $rabbit_password

echo Postgres - user $db_username password $db_password

# Data Flow Server database secret
kubectl create secret generic dataflow-server-db-secret \
  --from-literal=host=postgres-db \
  --from-literal=port=5432 \
--from-literal=username=postgres \
  --from-literal=password=postgres \
  --from-literal=database=postgres

  --from-literal=username="$db_username" \
  --from-literal=password="$db_password" \
  --from-literal=database=postgres-db


  kubectl create secret generic rabbitmq-secret \
    --from-literal=host=rabbitmq \
    --from-literal=port=5672 \
    --from-literal=username="$rabbit_username" \
    --from-literal=password="$rabbit_password"



kubectl create secret generic skipper-server-db-secret \
  --from-literal=host=postgres-db \
  --from-literal=port=5432 \
--from-literal=username=postgres \
  --from-literal=password=postgres \
  --from-literal=database=postgres



helm install dataflow-release oci://tanzu-dataflow.packages.broadcom.com/dataflow \
  --version "2.0.1" \
  --set database.mysql.enabled=false \
  --set database.postgresql.enabled=false \
  --set externalDatabase.enabled=true \
  --set externalDatabase.server.type=postgresql \
  --set externalDatabase.server.port=5432 \
  --set externalDatabase.server.secret=dataflow-server-db-secret \
  --set externalDatabase.skipper.type=postgresql \
  --set externalDatabase.skipper.port=5432\
  --set externalDatabase.skipper.secret=skipper-server-db-secret \
  --set rabbitmq.enabled=false \
  --set externalRabbitmq.enabled=true \
  --set externalRabbitmq.port=5672 \
  --set externalRabbitmq.secret=rabbitmq-secret \
  --timeout 10m \
  --wait

#    helm install dataflow-release oci://tanzu-dataflow.packages.broadcom.com/dataflow \
#      --version "2.0.1" \
#      --set database.mysql.enabled=false \
#      --set database.postgresql.enabled=true \
#      --namespace tanzu-data  \
#      --timeout 10m \
#      --wait