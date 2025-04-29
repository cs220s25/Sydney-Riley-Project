#!/bin/bash

echo "Starting the Discord bot deployment..."

# Build the docker image
docker build -t dbot .

# Run the docker image
docker run -d --name dbot-container -p 8080:8080 dbot

# Run the docker container.
docker-compose up
