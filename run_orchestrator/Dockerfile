FROM ubuntu:18.04

# Install the necessary packages
RUN apt update && apt -y upgrade
RUN apt -y install python3 
RUN apt -y install python3-pip
RUN apt -y install libgl-dev
RUN pip3 install --upgrade setuptools

# Install the dependencies
RUN pip3 install Flask
RUN pip3 install marshmallow
RUN pip3 install pika
RUN pip3 install sqlalchemy
RUN pip3 install argparse

# Create folder for the project
RUN mkdir /app

# Copy the .jar to the container
COPY * /app/

# Switch directory context to /app
WORKDIR /app

# Run the application
ENTRYPOINT ["python3", "app.py"]
