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
COPY target/producer-1.0-SNAPSHOT-jar-with-dependencies.jar /app/

# Switch directory context to /app
WORKDIR /app

# Run the application
CMD ["java", "-cp", "producer-1.0-SNAPSHOT-jar-with-dependencies.jar", "App", "-olts", "10", "-messages", "100", "-queue_host", "broker_queue", "-queue_port", "5672", "-directly_from_broker_queue", "0"]
