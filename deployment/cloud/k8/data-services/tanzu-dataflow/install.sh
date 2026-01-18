export POSTGRESQL_SUPPORT_TOKEN=$TANZU_POSTGRES_FOR_K8_PASSWORD

helm registry login tanzu-dataflow.packages.broadcom.com \
  --username=$BROADCOM_USERNAME \
  --password=${DATAFLOW_SUPPORT_TOKEN}


  kubectl create namespace dataflow

  kubectl create secret docker-registry tanzu-dataflow-registry-secret \
    --docker-server=registry.packages.broadcom.com \
    --docker-username=$BROADCOM_USERNAME \
    --docker-password="${DATAFLOW_SUPPORT_TOKEN}" \
    --namespace dataflow


    helm install dataflow-release oci://tanzu-dataflow.packages.broadcom.com/dataflow \
      --version "2.0.1" \
      --set database.mysql.enabled=false \
      --set database.postgresql.enabled=true \
      --namespace dataflow \
      --timeout 10m \
      --wait