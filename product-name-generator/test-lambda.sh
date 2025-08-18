#!/bin/bash

echo "Testing Product Name Generator Lambda Function..."
echo ""

echo "1. Testing Health Endpoint..."
curl -s "http://localhost:3000/health"
echo ""
echo ""

echo "2. Testing Categories Endpoint..."
curl -s "http://localhost:3000/categories"
echo ""
echo ""

echo "3. Testing Simple Name Generation..."
curl -s "http://localhost:3000/api/generate-name/simple?keywords=wireless,bluetooth&category=electronics&count=3"
echo ""
echo ""

echo "4. Testing POST Name Generation..."
curl -s -X POST "http://localhost:3000/api/generate-name" \
  -H "Content-Type: application/json" \
  -d '{"keywords":["smart","home"],"category":"home","count":5}'
echo ""
echo ""

echo "All tests completed!"