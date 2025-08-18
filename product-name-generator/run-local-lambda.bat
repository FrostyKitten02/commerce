@echo off
echo Starting Product Name Generator as Local Lambda Function...

REM Check if SAM CLI is installed
sam --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: AWS SAM CLI is not installed!
    echo Please install it from: https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html
    pause
    exit /b 1
)

REM Build the project
echo Building Maven project...
mvn clean package -q
if %errorlevel% neq 0 (
    echo ERROR: Maven build failed!
    pause
    exit /b 1
)

REM Start local Lambda function
echo Starting local Lambda function on http://localhost:3000
echo.
echo Available endpoints:
echo   POST http://localhost:3000/api/generate-name
echo   GET  http://localhost:3000/api/generate-name/simple?keywords=wireless,phone^&category=electronics
echo   GET  http://localhost:3000/health
echo   GET  http://localhost:3000/categories
echo.
echo Press Ctrl+C to stop the function
echo.

sam local start-api --host 0.0.0.0 --port 3000