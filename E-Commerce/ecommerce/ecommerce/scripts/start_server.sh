#!/bin/bash

# Start the agent
sudo service codedeploy-agent start
sudo systemctl enable codedeploy-agent

echo "Starting Spring Boot application..."
nohup java -jar /target/ecommerce-0.0.1-SNAPSHOT.jar > /target/app.log 2>&1 &
