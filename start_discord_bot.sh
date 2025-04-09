#!/bin/bash

echo "Starting the Discord bot..."

echo "Updating Maven dependencies..."
mvn clean install

echo "Starting the bot using Maven..."
mvn exec:java -Dexec.mainClass="edu.moravian.dbot.WordScrambleBot"

echo "The bot is now running. Press CTRL+C to stop it."

