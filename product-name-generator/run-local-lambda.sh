#!/bin/bash

echo "Starting Product Name Generator as Local Lambda Function..."

# Check if SAM CLI is installed
if ! command -v sam &> /dev/null; then
    echo "ERROR: AWS SAM CLI is not installed!"
    echo "Please install it from: https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html"
    exit 1
fi

# Build the project
echo "Building Maven project..."
mvn clean package -q
if [ $? -ne 0 ]; then
    echo "ERROR: Maven build failed!"
    exit 1
fi

# Start local Lambda function
echo "Starting local Lambda function on http://localhost:3000"
echo ""
echo "Available endpoints:"
echo "  POST http://localhost:3000/api/generate-name"
echo "  GET  http://localhost:3000/api/generate-name/simple?keywords=wireless,phone&category=electronics"
echo "  GET  http://localhost:3000/health"
echo "  GET  http://localhost:3000/categories"
echo ""
echo "Press Ctrl+C to stop the function"
echo ""

sam local start-api --host 0.0.0.0 --port 3000