#!/bin/bash

docker cp ./dumps/$1.dump relational-db:/$1.dump
docker exec -it relational-db pg_restore -d results -h 127.0.0.1 -U postgres $1.dump
