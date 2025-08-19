@echo off
echo Building Product Name Generator...
cd product-name-generator
mvn clean package -DskipTests
cd ..
echo Build completed!