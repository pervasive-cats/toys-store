#!/bin/bash

set -euo pipefail;

if [[ "$#" != "6" ]]; then
    echo "Needed six parameters, three pairs of usernames and passwords";
    exit 1;
fi;

DB_USERNAME=$1;
DB_PASSWORD=$2;
RMQ_USERNAME=$3;
RMQ_PASSWORD=$4;
DT_USERNAME=$5;
DT_PASSWORD=$6;

docker compose -f ./ditto/docker-compose.yml up -d;

while true; do
    CONTAINERS=$(docker ps -a | { grep 'ditto' || true; } | { grep '(healthy)' || true; } | sed -rn 's/^.*?\s([a-z0-9\-]+)$/\1/p');
    if [[ "$CONTAINERS" == *"ditto-things-search-1"* ]] && [[ "$CONTAINERS" == *"ditto-things-1"* ]] && [[ "$CONTAINERS" == *"ditto-connectivity-1"* ]] && [[ "$CONTAINERS" == *"ditto-gateway-1"* ]] && [[ "$CONTAINERS" == *"ditto-policies-1"* ]];
    then
      break;
    fi;
    sleep 5;
done;

echo "Ditto started";

RABBIT_CONTAINER=$(docker run -e RABBITMQ_DEFAULT_USER="$RMQ_USERNAME" -e RABBITMQ_DEFAULT_PASS="$RMQ_PASSWORD" --network="host" -d rabbitmq:3.11.7);

while true; do
    LOG_LINES=$(docker logs $RABBIT_CONTAINER | { grep "Server startup complete" || true; } | wc -l);
    if [[ "$LOG_LINES" > "0" ]];
    then
      break;
    fi;
    sleep 5;
done;

echo "RabbitMQ started";

MICROSERVICES=("users" "items" "carts");

for INDEX in "${!MICROSERVICES[@]}"; do
    MICROSERVICE_NAME=${MICROSERVICES[$INDEX]};
    SERVER_PORT_NUMBER=$((8082 + "$INDEX"));
    touch ./"$MICROSERVICE_NAME"/application.conf;
    if [[ "$MICROSERVICE_NAME" == "carts" ]]; then
        cat > ./"$MICROSERVICE_NAME"/application.conf <<-EOF
repository {
    dataSourceClassName = org.postgresql.ds.PGSimpleDataSource
    dataSource {
        user = $DB_USERNAME
        password = $DB_PASSWORD
        databaseName = $MICROSERVICE_NAME
        portNumber = 5432
        serverName = localhost
    }
    connectionTimeout = 30000
}
server {
    portNumber = $SERVER_PORT_NUMBER
    hostName = localhost
}
messageBroker {
    username = $RMQ_USERNAME
    password = $RMQ_PASSWORD
    virtualHost = "/"
    portNumber = 5672
    hostName = localhost
}
ditto {
    hostName = localhost
    portNumber = 8080
    username = $DT_USERNAME
    password = $DT_PASSWORD
    namespace = io.github.pervasivecats
    thingModel = "https://raw.githubusercontent.com/pervasive-cats/toys-store-carts/main/cart.jsonld"
}
EOF
    else
        cat > ./"$MICROSERVICE_NAME"/application.conf <<-EOF
repository {
    dataSourceClassName = org.postgresql.ds.PGSimpleDataSource
    dataSource {
        user = $DB_USERNAME
        password = $DB_PASSWORD
        databaseName = $MICROSERVICE_NAME
        portNumber = 5432
        serverName = localhost
    }
    connectionTimeout = 30000
}
server {
    portNumber = $SERVER_PORT_NUMBER
    hostName = localhost
}
messageBroker {
    username = $RMQ_USERNAME
    password = $RMQ_PASSWORD
    virtualHost = "/"
    portNumber = 5672
    hostName = localhost
}
EOF
    fi;
done;

docker compose -f ./docker-compose.demo.yml up -d;

echo "Services started";
    