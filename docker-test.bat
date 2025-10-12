@echo off
REM ====================================
REM Build và Test Docker Image locally
REM ====================================

echo 🐳 Building Docker image...
docker build -t bus-booking-app .

if %ERRORLEVEL% neq 0 (
    echo ❌ Docker build failed!
    pause
    exit /b 1
)

echo ✅ Docker build successful!
echo.

echo 🚀 Starting container...
echo 📧 Email config: Set your EMAIL_PASSWORD environment variable
echo 🗄️ Database: Configure your DB connection
echo.

REM Chạy container với environment variables
docker run -it --rm ^
    -p 8080:8080 ^
    -e EMAIL_FROM=your_email@gmail.com ^
    -e EMAIL_PASSWORD=your_app_password ^
    -e DB_URL=jdbc:postgresql://host.docker.internal:5432/busbooking ^
    -e DB_USERNAME=postgres ^
    -e DB_PASSWORD=your_db_password ^
    --name bus-booking-container ^
    bus-booking-app

pause