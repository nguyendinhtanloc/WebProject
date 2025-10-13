#!/bin/bash

echo "========================================"
echo "    Employee Customer Chat System"
echo "========================================"
echo

echo "[1/4] Checking Java version..."
java -version
if [ $? -ne 0 ]; then
    echo "ERROR: Java not found! Please install Java 11 or higher."
    exit 1
fi

echo
echo "[2/4] Checking Maven installation..."
mvn -version
if [ $? -ne 0 ]; then
    echo "ERROR: Maven not found! Please install Maven."
    exit 1
fi

echo
echo "[3/4] Building project..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "ERROR: Build failed! Please check the logs above."
    exit 1
fi

echo
echo "[4/4] Starting Tomcat server..."
echo
echo "========================================"
echo "    Server will start on port 8080"
echo "    Open browser: http://localhost:8080/chat"
echo "========================================"
echo
echo "Press Ctrl+C to stop the server"
echo

mvn tomcat7:run

