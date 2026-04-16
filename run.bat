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

REM Compile the application
echo Compiling Java files...
javac -cp "target\dependency\*" -d target\classes src\main\java\com\nutritrack\*.java src\main\java\com\nutritrack\**\*.java 2>nul

if %errorlevel% neq 0 (
    echo Compilation failed. Using simple server instead...
    java -cp "target\classes;target\dependency\*" com.nutritrack.NutriTrackApplication
) else (
    echo Compilation successful! Starting Spring Boot...
    java -cp "target\classes;target\dependency\*" com.nutritrack.NutriTrackApplication
)

echo.
echo ========================================
echo Application should be running at:
echo http://localhost:8080
echo H2 Console: http://localhost:8080/h2-console
echo ========================================
pause
