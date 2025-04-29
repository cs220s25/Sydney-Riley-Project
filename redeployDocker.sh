#!/bin/bash

echo "Restarting the Discord bot deployment..."

# Stop and remove docker components
sudo docker compose down

# Pull latest version of repository
sudo git pull origin main

# Rebuild docker images and run containers
sudo docker compose up --build -d
