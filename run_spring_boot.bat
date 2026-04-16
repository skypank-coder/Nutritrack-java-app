@echo off
echo ========================================
echo NutriTrack Spring Boot Application
echo ========================================
echo.

echo Checking Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Java not found. Please install Java 17 or higher.
    echo Download from: https://adoptium.net/
    pause
    exit /b 1
)

echo Java found! Starting Spring Boot application...
echo.

REM Check if Maven is available
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven not found. Please install Maven or use run_simple.bat for basic server.
    echo Download Maven from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo Building and running Spring Boot application...
echo This may take a few moments on first run...
echo.

call mvn clean spring-boot:run

echo.
echo ========================================
echo NutriTrack Spring Boot Application Started!
echo Access the application at: http://localhost:8080
echo H2 Console: http://localhost:8080/h2-console
echo ========================================
pause
