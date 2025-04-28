#!/bin/bash

echo "Restarting the Discord bot deployment..."

# Pull lastest code from repository
sudo git pull origin main

# Clean and Package the project
sudo mvn clean package

# Restart the service
sudo systemctl restart dbot.service
