FROM node:18

# Install necessary packages
RUN apt update && apt -y upgrade

# Create folder for the project
RUN mkdir /app

# Copy app folder
COPY  . /app/.

# Set workdir
WORKDIR /app

# Install serve
RUN npm install

# Run the app
ENTRYPOINT [ "npm", "start"]
