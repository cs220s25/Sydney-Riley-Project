#!/bin/bash

# Install Docker
yum install -y docker

# Enable Docker service to start at boot
systemctl enable docker

# Start Docker service now
systemctl start docker

# Install Docker Compose manually (v2.35.1)
mkdir -p /usr/local/lib/docker/cli-plugins
curl -SL https://github.com/docker/compose/releases/download/v2.35.1/docker-compose-linux-x86_64 -o /usr/local/lib/docker/cli-plugins/docker-compose
chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

# Add ec2-user to the docker group so you can run docker without sudo
usermod -aG docker ec2-user

# Install Git to clone your project
yum install -y git

# Clone your GitHub project (Sydney-Riley-Project)
git clone https://github.com/cs220s25/Sydney-Riley-Project.git

# Change directory into the project
cd /Sydney-Riley-Project

# Make sure redeploy.sh is executable
chmod +x /redeployDocker.sh

# Run docker-compose to start your app (in detached mode)
docker compose up -d
