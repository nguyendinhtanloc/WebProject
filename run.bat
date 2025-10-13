@echo off
echo ========================================
echo    Employee Customer Chat System
echo ========================================
echo.

echo [1/4] Checking Java version...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java not found! Please install Java 11 or higher.
    pause
    exit /b 1
)

echo.
echo [2/4] Checking Maven installation...
mvn -version
if %errorlevel% neq 0 (
    echo ERROR: Maven not found! Please install Maven.
    pause
    exit /b 1
)

echo.
echo [3/4] Building project...
call mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Build failed! Please check the logs above.
    pause
    exit /b 1
)

echo.
echo [4/4] Starting Tomcat server...
echo.
echo ========================================
echo    Server will start on port 8080
echo    Open browser: http://localhost:8080/chat
echo ========================================
echo.
echo Press Ctrl+C to stop the server
echo.

call mvn tomcat7:run

