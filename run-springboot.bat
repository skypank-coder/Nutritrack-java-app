@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Starting Spring Boot NutriTrack Application...
echo.

cd /d "%~dp0"

echo Compiling with Maven...
call mvn.cmd clean compile -q

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Running Spring Boot application...
echo.
echo Access the application at: http://localhost:8080
echo H2 Console: http://localhost:8080/h2-console
echo.
call mvn.cmd spring-boot:run

pause
