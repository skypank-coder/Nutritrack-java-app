@echo off
echo ========================================
echo NutriTrack Web Application Setup
echo ========================================
echo.

echo Checking if Maven is available...
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven not found. Please install Maven or use Maven wrapper.
    echo.
    echo To install Maven:
    echo 1. Download from: https://maven.apache.org/download.cgi
    echo 2. Extract to a location (e.g., C:\maven)
    echo 3. Add MAVEN_HOME environment variable
    echo 4. Add %MAVEN_HOME%\bin to PATH
    echo.
    echo Or use the manual compilation method below.
    pause
    exit /b 1
)

echo Maven found. Starting Spring Boot application...
echo.

mvn spring-boot:run

echo.
echo ========================================
echo Application stopped
echo ========================================
echo.
echo Access the application at: http://localhost:8081
echo H2 Console: http://localhost:8081/h2-console
echo.
pause
