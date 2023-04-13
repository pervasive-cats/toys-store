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

MICROSERVICES=("users" "items" "carts" "stores" "shopping" "payments");

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
        serverName = postgres_$MICROSERVICE_NAME
    }
    connectionTimeout = 30000
}
server {
    portNumber = $SERVER_PORT_NUMBER
    hostName = 0.0.0.0
}
messageBroker {
    username = $RMQ_USERNAME
    password = $RMQ_PASSWORD
    virtualHost = "/"
    portNumber = 5672
    hostName = rabbitmq
}
ditto {
    hostName = ditto-things-1
    portNumber = 8080
    username = $DT_USERNAME
    password = $DT_PASSWORD
    namespace = io.github.pervasivecats
    thingModel = "https://raw.githubusercontent.com/pervasive-cats/toys-store-carts/main/cart.jsonld"
}
EOF
    elif [[ "$MICROSERVICE_NAME" == "stores" ]]; then
        cat > ./"$MICROSERVICE_NAME"/application.conf <<-EOF
repository {
    dataSourceClassName = org.postgresql.ds.PGSimpleDataSource
    dataSource {
        user = $DB_USERNAME
        password = $DB_PASSWORD
        databaseName = $MICROSERVICE_NAME
        portNumber = 5432
        serverName = postgres_$MICROSERVICE_NAME
    }
    connectionTimeout = 30000
}
server {
    portNumber = $SERVER_PORT_NUMBER
    hostName = 0.0.0.0
}
messageBroker {
    username = $RMQ_USERNAME
    password = $RMQ_PASSWORD
    virtualHost = "/"
    portNumber = 5672
    hostName = rabbitmq
}
ditto {
    hostName = ditto-things-1
    portNumber = 8080
    username = $DT_USERNAME
    password = $DT_PASSWORD
    namespace = io.github.pervasivecats
    thingModelAntiTheftSystem = "https://raw.githubusercontent.com/pervasive-cats/toys-store-stores/main/antiTheftSystem.jsonld"
    thingModelDropSystem = "https://raw.githubusercontent.com/pervasive-cats/toys-store-stores/main/dropSystem.jsonld"
    thingModelShelving = "https://raw.githubusercontent.com/pervasive-cats/toys-store-stores/main/shelving.jsonld"
}
itemServer {
    hostName = items
    portNumber = 8083
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
        serverName = postgres_$MICROSERVICE_NAME
    }
    connectionTimeout = 30000
}
server {
    portNumber = $SERVER_PORT_NUMBER
    hostName = 0.0.0.0
}
messageBroker {
    username = $RMQ_USERNAME
    password = $RMQ_PASSWORD
    virtualHost = "/"
    portNumber = 5672
    hostName = rabbitmq
}
EOF
    fi;
done;

export RMQ_USERNAME
export RMQ_PASSWORD
export DB_USERNAME
export DB_PASSWORD

docker compose up -d;

echo "Services started";
    