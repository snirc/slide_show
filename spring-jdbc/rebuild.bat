@echo off
SETLOCAL

REM --- Step 1: Build the project
echo.
echo 🔧 Building Maven project...
mvn clean package

IF %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven build failed.
    EXIT /B 1
)

REM --- Step 2: Build Docker image
echo.
echo 🐳 Building Docker image...
docker build -t your-spring-app .

IF %ERRORLEVEL% NEQ 0 (
    echo ❌ Docker build failed.
    EXIT /B 1
)

REM --- Step 3: Restart the app service with docker-compose
echo.
echo 🔁 Restarting Docker container...
docker-compose up -d --build app

IF %ERRORLEVEL% EQU 0 (
    echo ✅ App container restarted successfully.
) ELSE (
    echo ❌ Failed to start container.
    EXIT /B 1
)

ENDLOCAL
