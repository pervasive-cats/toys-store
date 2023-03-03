#!/bin/bash

set -euo pipefail;

if [[ "$#" != "2" ]]; then
    echo "Needed two parameters, a username and a password";
    exit 1;
fi;

USERNAME=$1;
PASSWORD=$2;

MICROSERVICES=("users" "items" "carts");

for INDEX in ${!MICROSERVICES[@]}; do
    MICROSERVICE_NAME=${MICROSERVICES[$INDEX]};
    SERVER_PORT_NUMBER=$(expr 8081 + $INDEX);
    touch ./$MICROSERVICE_NAME/application.conf;
    cat > ./$MICROSERVICE_NAME/application.conf <<-EOF
		repository {
		    dataSourceClassName = org.postgresql.ds.PGSimpleDataSource
		    dataSource {
		        user = $USERNAME
		        password = $PASSWORD
		        databaseName = $MICROSERVICE_NAME
		        portNumber = 5432
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
    