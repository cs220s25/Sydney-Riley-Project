#!/bin/bash

echo "Starting the Discord bot deployment..."

# Set the environment variables
export REDIS_HOST=localhost
export REDIS_PORT=6379

# Start the Redis service
brew services start redis

# Clean and Package project
mvn clean package

# Run the Discord Bot
java -jar target/dbot-1.0-SNAPSHOT-jar-with-dependencies.jar
