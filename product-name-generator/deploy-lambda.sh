#!/bin/bash

# AWS Lambda Deployment Script for Product Name Generator

echo "Building Product Name Generator for AWS Lambda deployment..."

# Build the project
mvn clean package

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "JAR file created: target/product-name-generator-0.0.1-SNAPSHOT.jar"
    echo ""
    echo "To deploy to AWS Lambda:"
    echo "1. Upload target/product-name-generator-0.0.1-SNAPSHOT.jar to AWS Lambda"
    echo "2. Set handler: si.afridau.commerce.productname.lambda.ProductNameLambdaHandler"
    echo "3. Set runtime: Java 21"
    echo "4. Configure memory: 512MB (recommended)"
    echo "5. Set timeout: 30 seconds"
    echo ""
    echo "Environment variables (optional):"
    echo "- No environment variables required"
    echo ""
    echo "API Gateway integration:"
    echo "- Method: ANY"
    echo "- Path: /{proxy+}"
    echo "- Enable CORS"
else
    echo "Build failed!"
    exit 1
fi