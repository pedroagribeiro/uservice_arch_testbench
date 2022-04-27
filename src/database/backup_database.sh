#!/bin/bash

docker exec -it relational-db pg_dump -Fc -h 127.0.0.1 -U postgres results -f $1.dump
docker cp relational-db:/$1.dump .
