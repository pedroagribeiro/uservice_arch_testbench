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
    image: 10.112.106.236:32500/ztp-onu-gui:1.0
    container_name: testbench-gui
    ports:
      - \"55555:3000\"
    depends_on:
      - producer
    networks:
      - private_network
  producer_queue:
    image: 10.112.106.236:32500/ztp-onu-rabbitmq:1.0
    container_name: producer-queue
    ports:
      - \"5679:5672\"
      - \"15679:15672\"
    networks:
      - private_network
  broker_queue:
    image: 10.112.106.236:32500/ztp-onu-rabbitmq:1.0
    container_name: broker-queue
    hostname: broker-queue
    ports:
      - "5675:5672"
      - "15675:15672"
    networks:
      - private_network
  producer:
    image: 10.112.106.236:32500/ztp-onu-producer_spring:1.0
    container_name: producer
    ports:
      - "55556:8080"
    depends_on:
      - producer_queue
      - relational_database
    networks:
      - private_network
  broker:
    image: 10.112.106.236:32500/ztp-onu-broker_spring:1.0
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
    image: 10.112.106.236:32500/ztp-onu-worker_spring:1.0
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
    image: 10.112.106.236:32500/ztp-onu-olt_spring:1.0
    command: \"--olt.id=%d\" 
    container_name: olt-%d
    depends_on:
      - olts_queue
    networks:
      - private_network""" %(i, i, i)

last_part = """
  workers_queue:
    image: 10.112.106.236:32500/ztp-onu-rabbitmq:1.0
    container_name: workers-queue
    ports:
      - "5672:5672"
      - "15672:15672"
    networks:
      - private_network
  olts_queue:
    image: 10.112.106.236:32500/ztp-onu-rabbitmq:1.0
    container_name: olts-queue
    ports:
      - "5673:5672"
      - "15673:15672"
    networks:
      - private_network
  relational_database:
    image: 10.112.106.236:32500/ztp-onu-relational-database:1.0
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