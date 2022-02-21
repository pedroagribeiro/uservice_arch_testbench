FROM ubuntu:18.04
ARG ID

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
COPY target/worker-1.0-SNAPSHOT-jar-with-dependencies.jar /app/

# Switch directory context to /app
WORKDIR /app

RUN echo "$ID" > teste.txt 

# Run the application
ENTRYPOINT ["java", "-cp", "worker-1.0-SNAPSHOT-jar-with-dependencies.jar", "App", "-containerized", "-id"]
