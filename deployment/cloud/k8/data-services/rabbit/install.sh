helm registry login rabbitmq-helmoci.packages.broadcom.com -u $BROADCOM_USERNAME -p $RABBITMQ_SUPPORT_TOKEN

kubectl create namespace rabbitmq-system


kubectl create secret docker-registry tanzu-rabbitmq-registry-creds --docker-server \
"rabbitmq-operator.packages.broadcom.com" --docker-username $BROADCOM_USERNAME --docker-password $RABBITMQ_SUPPORT_TOKEN -n rabbitmq-system


kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.19.1/cert-manager.yaml


helm install tanzu-rabbitmq oci://rabbitmq-helmoci.packages.broadcom.com/tanzu-rabbitmq-operators \
--namespace rabbitmq-system


kubectl get pods -w -n rabbitmq-system