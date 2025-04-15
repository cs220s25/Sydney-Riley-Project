#!/bin/bash

# Install Redis
yum install redis6 -y

# Enable Redis service
systemctl enable redis6

#Start Redis service
systemctl start redis6

# Install Maven with Amazon Corretto 21 JDK support
yum install -y maven-amazon-corretto21

# Install Git
yum install git -y

# Clone the repository
git clone https://github.com/cs220s25/Sydney-Riley-Project /Sydney-Riley-Project

# Change to the project directory
cd /Sydney-Riley-Project

# Clean and package project
mvn clean package

# Copy the service file
cp dbot.service /etc/systemd/system

# Reload the sysemd manager configuration
systemctl daemon-reload

# Enable the service
systemctl enable dbot.service 

# Start the service
systemctl start dbot.service
