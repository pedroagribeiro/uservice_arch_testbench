import os

if os.path.exists("custom-compose.yml"):
    os.remove("custom-compose.yml")
else:
    print("The file does not exist")

workers = 150
olts = 100

f = open("custom-compose.yml", "w+")

first_part = """version: \"3\"

services:
  gui:
    build:
      context: ../src/gui
    container_name: testbench-gui
    ports:
      - \"30000:3000\"
    depends_on:
      - producer
    networks:
      - private_network
  producer_queue:
    build:
      context: ../src/rabbitmq
    container_name: producer-queue
    ports:
      - \"5679:5672\"
      - \"15679:15672\"
    networks:
      - private_network
  broker_queue:
    build:
      context: ../src/rabbitmq
    container_name: broker-queue
    hostname: broker-queue
    ports:
      - "5675:5672"
      - "15675:15672"
    networks:
      - private_network
  producer:
    build:
      context: ../src/producer
    container_name: producer
    ports:
      - "55556:8080"
    depends_on:
      - producer_queue
      - relational_database
    networks:
      - private_network
  broker:
    build:
      context: ../src/broker
    container_name: broker
    ports:
      - \"8081:8081\"
    depends_on:
      - broker_queue
      - relational_database
    networks:
      - private_network"""

workers_block = ""

for i in range(workers):
    workers_block += """
  worker_%d:
    build:
      context: ../src/worker
    command: \"--worker.id=%d\"
    container_name: worker-%d
    depends_on:
      - workers_queue
      - relational_database
    networks:
      - private_network""" %(i, i, i)

olts_block = ""

for i in range(olts):
    olts_block += """
  olt_%d:
    build:
      context: ../src/olt
    command: \"--olt.id=%d\" 
    container_name: olt-%d
    depends_on:
      - olts_queue
    networks:
      - private_network""" %(i, i, i)

last_part = """
  workers_queue:
    build:
      context: ../src/rabbitmq
    container_name: workers-queue
    ports:
      - "5672:5672"
      - "15672:15672"
    networks:
      - private_network
  olts_queue:
    build:
      context: ../src/rabbitmq
    container_name: olts-queue
    ports:
      - "5673:5672"
      - "15673:15672"
    networks:
      - private_network
  relational_database:
    build:
      context: ../src/database
    container_name: relational-db
    ports:
      - "5432:5432"
    networks:
      - private_network
networks:
  private_network:
    driver: bridge"""

f.write(first_part)
f.write(workers_block)
f.write(olts_block)
f.write(last_part)