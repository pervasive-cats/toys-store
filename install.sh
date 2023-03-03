#!/bin/bash

set -euo pipefail;

if [[ "$#" != "2" ]]; then
    echo "Needed two parameters, a username and a password";
    exit 1;
fi;

USERNAME=$1;
PASSWORD=$2;

docker compose -f ./ditto/docker-compose.yml up -d;

while true; do
    CONTAINERS=$(docker ps -a | { grep 'ditto' || true; } | { grep '(healthy)' || true; } | sed -rn 's/^.*?\s([a-z0-9\-]+)$/\1/p');
    [[ "$CONTAINERS" != *"ditto-things-search-1"* ]] && [[ "$CONTAINERS" != *"ditto-things-1"* ]] && [[ "$CONTAINERS" != *"ditto-connectivity-1"* ]] && [[ "$CONTAINERS" != *"ditto-gateway-1"* ]] && [[ "$CONTAINERS" != *"ditto-policies-1"* ]] || break;
    sleep 5;
done;

echo "Ditto started";

MICROSERVICES=("users" "items" "carts" "stores" "shopping" "payments");

for INDEX in ${!MICROSERVICES[@]}; do
    MICROSERVICE_NAME=${MICROSERVICES[$INDEX]};
    SERVER_PORT_NUMBER=$(expr 8081 + $INDEX);
    POSTGRES_PORT_NUMBER=$(expr 5433 + $INDEX);
    touch ./$MICROSERVICE_NAME/application.conf;
    cat > ./$MICROSERVICE_NAME/application.conf <<-EOF
		repository {
		    dataSourceClassName = org.postgresql.ds.PGSimpleDataSource
		    dataSource {
		        user = $USERNAME
		        password = $PASSWORD
		        databaseName = $MICROSERVICE_NAME
		        portNumber = $POSTGRES_PORT_NUMBER
		        serverName = "0.0.0.0"
		    }
		    connectionTimeout = 30000
		}
		server {
		    portNumber = $SERVER_PORT_NUMBER
		    hostName = "0.0.0.0"
		}
		messageBroker {
		    username = $USERNAME
		    password = $PASSWORD
		    virtualHost = "/"
		    portNumber = 5672
		    hostName = "0.0.0.0"
		}
	EOF
done;

docker compose up -d;

echo "Services started";
    