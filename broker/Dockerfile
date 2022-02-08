FROM ubuntu:18.04

# Install necessary packages
RUN apt update && apt -y upgrade
RUN apt -y install openjdk-11-jdk
RUN apt -y install maven

# Installing Java 11
RUN export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/jre/bin
RUN export PATH=$JAVA_HOME/bin:$PATH

# Create folder for the project
RUN mkdir /app

# Copy the .jar to the container
COPY target/broker-1.0-SNAPSHOT-jar-with-dependencies.jar /app/

# Swith directory context to /app
WORKDIR /app

# Run the application
CMD ["java", "-cp", "broker-1.0-SNAPSHOT-jar-with-dependencies.jar", "App", "-queue message_queue -workers 3 -redis_host localhost -redis_port 6379 -workers_base_port 5672 -queue_host localhost -queue_port 5675 -algorithm 1"]
