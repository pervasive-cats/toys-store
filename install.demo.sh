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

MICROSERVICES=("users" "items" "carts");

for INDEX in ${!MICROSERVICES[@]}; do
    MICROSERVICE_NAME=${MICROSERVICES[$INDEX]};
    SERVER_PORT_NUMBER=$(expr 8881 + $INDEX);
    touch ./$MICROSERVICE_NAME/application.conf;
    cat > ./$MICROSERVICE_NAME/application.conf <<-EOF
		repository {
		    dataSourceClassName = org.postgresql.ds.PGSimpleDataSource
		    dataSource {
		        user = $DB_USERNAME
		        password = $DB_PASSWORD
		        databaseName = $MICROSERVICE_NAME
		        portNumber = 5432
		        serverName = 0.0.0.0
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
		    hostName = 0.0.0.0
		}
		ditto {
		    hostName = 0.0.0.0
		    portNumber = 8080
		    username = $DT_USERNAME
		    password = $DT_PASSWORD
		    namespace = io.github.pervasivecats
		    thingModel = "https://raw.githubusercontent.com/pervasive-cats/toys-store-carts/main/cart.jsonld"
		}
	EOF
done;

docker compose -f ./docker-compose.demo.yml up -d;

echo "Services started";
    