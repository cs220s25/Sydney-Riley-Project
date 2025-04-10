#!/bin/bash

echo "Starting the Discord bot deployment..."

brew services start redis

mvn clean package

java -jar target/dbot-1.0-SNAPSHOT-jar-with-dependencies.jar


