#!/bin/bash

# Build all Java services
echo "Building Java services..."

echo "Building auth service..."
cd auth
mvn clean package -DskipTests
cd ..

echo "Building catalog service..."
cd catalog  
mvn clean package -DskipTests
cd ..

echo "Building cart service..."
cd cart
mvn clean package -DskipTests
cd ..

echo "Building checkout service..."
cd checkout
mvn clean package -DskipTests
cd ..

echo "Building auth-common..."
cd auth-common
mvn clean install -DskipTests
cd ..

echo "Building exception-handling..."
cd exception-handling
mvn clean install -DskipTests
cd ..

echo "Building web-app..."
cd web-app
npm install
npm run build
cd ..

echo "All services built successfully!"
echo "Run 'docker-compose up --build' to start all services"