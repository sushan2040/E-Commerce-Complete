#!/bin/bash
echo "Stopping existing Spring Boot application..."
pkill -f 'java -jar /target/ecommerce-0.0.1-SNAPSHOT.jar' || echo "No application running"
