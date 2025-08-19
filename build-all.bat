@echo off

echo Building Java services...

echo Building auth-common...
cd auth-common
call mvn clean install -DskipTests
cd ..

echo Building exception-handling...
cd exception-handling
call mvn clean install -DskipTests
cd ..

echo Building auth service...
cd auth
call mvn clean install -DskipTests
cd ..

echo Building catalog service...
cd catalog  
call mvn clean install -DskipTests
cd ..

echo Building cart service...
cd cart
call mvn clean install -DskipTests
cd ..

echo Building checkout service...
cd checkout
call mvn clean install -DskipTests
cd ..

echo Building web-app...
cd web-app
call npm install
call npm run build
cd ..

echo All services built successfully!
echo Run 'docker-compose up --build' to start all services