#!/bin/bash

echo "Starting the Discord bot deployment..."

# Start the Redis service
brew services start redis

# Clean and Package project
mvn clean package

# Load data for project
mvn exec:java -Dexec.mainClass="edu.moravian.LoadData"

# Run the Discord Bot
java -jar target/dbot-1.0-SNAPSHOT-jar-with-dependencies.jar
