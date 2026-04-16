@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM
@REM Required ENV vars:
@REM ------------------
@REM   JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM -----------------
@REM   M2_HOME - location of maven2's installed home dir
@REM   MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM   MAVEN_BATCH_PAUSE - set to 'on' to wait for a keystroke before ending
@REM   MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM   MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'
@echo off
@REM set title of command window
title %0
@REM enable echoing by setting MAVEN_BATCH_ECHO to 'on'
@if "%MAVEN_BATCH_ECHO%" == "on"  echo %MAVEN_BATCH_ECHO%

@REM set %HOME% to the root of the Maven installation
@if "%MAVEN_HOME%" == "" set "MAVEN_HOME=%~dp0..\"
@REM Execute a user defined script before this one
if not "%MAVEN_SKIP_RC%" == "" goto skipRcPre
@REM check for pre script, once with legacy .bat ending and once with .cmd ending
if exist "%USERPROFILE%\mavenrc_pre.bat" call "%USERPROFILE%\mavenrc_pre.bat" %*
if exist "%USERPROFILE%\mavenrc_pre.cmd" call "%USERPROFILE%\mavenrc_pre.cmd" %*
:skipRcPre

@setlocal

set ERROR_CODE=0

@REM To isolate internal variables from possible post scripts, we use another setlocal
@setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo Error: JAVA_HOME not found in your environment. >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto init

echo.
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

@REM ==== END VALIDATION ====

:init

@REM Find the project base dir, i.e. the directory that contains the folder ".mvn".
@REM Fallback to current working directory if not found.
set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
IF NOT "%MAVEN_PROJECTBASEDIR%"=="" (
    set "MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%"
)
IF NOT "%MAVEN_PROJECTBASEDIR%"=="" (
    set "MAVEN_PROJECTBASEDIR=%~dp0"
)
set "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%"

@REM Look for the Maven wrapper script
set "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
if exist "%WRAPPER_JAR%" goto wrapperJar

echo.
echo Error: Could not find Maven wrapper JAR at "%WRAPPER_JAR%" >&2
echo.
goto error

:wrapperJar
set "WRAPPER_DIR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper"
if exist "%WRAPPER_DIR%\maven-wrapper.properties" goto wrapperProperties

echo.
echo Error: Could not find Maven wrapper properties at "%WRAPPER_DIR%\maven-wrapper.properties" >&2
echo.
goto error

:wrapperProperties
@REM Read the maven-wrapper.properties file
@REM This file contains the location of the Maven wrapper jar and the Java home
@REM The format is: wrapperPath=path/to/maven-wrapper.jar
@REM The Java home is optional, but if not set, the wrapper will try to find it
@REM in the environment variable JAVA_HOME
@REM The wrapper will also try to find the Java home in the system path
for /f "tokens=1,* delims==" %%a in ("%WRAPPER_DIR%\maven-wrapper.properties") do (
    set "WRAPPER_PROPERTY=%%a"
    set "WRAPPER_VALUE=%%b"
    if "%WRAPPER_PROPERTY%"=="wrapperPath" set "WRAPPER_JAR=%WRAPPER_DIR%\%%b"
    if "%WRAPPER_PROPERTY%"=="javaHome" set "JAVA_HOME=%WRAPPER_DIR%\%%b"
)

@REM Validate the Java home
if not "%JAVA_HOME%" == "" goto OkJHomeWrapper
echo.
echo Error: JAVA_HOME not found in your environment. >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:OkJHomeWrapper
if exist "%JAVA_HOME%\bin\java.exe" goto wrapperJar

echo.
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:wrapperJar
set "MAVEN_JAVA_EXE=%JAVA_HOME%\bin\java.exe"

@REM Check if the Java executable exists
if exist "%MAVEN_JAVA_EXE%" goto javaExec

echo.
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:javaExec
@REM Execute Maven
set "MAVEN_CMD_LINE_ARGS=%*"

@REM Start with default JVM options
set "MAVEN_OPTS=-Xmx64m"

@REM Add JVM options for Maven
if not "%MAVEN_OPTS%" == "" goto mavenOpts
set "MAVEN_OPTS=%MAVEN_OPTS%"

:mavenOpts
@REM Add JVM options for Maven wrapper
if not "%MAVEN_WRAPPER_OPTS%" == "" goto wrapperOpts
set "MAVEN_OPTS=%MAVEN_OPTS% %MAVEN_WRAPPER_OPTS%"

:wrapperOpts
@REM Add JVM options for Maven wrapper
if not "%MAVEN_JAVA_OPTS%" == "" goto javaOpts
set "MAVEN_OPTS=%MAVEN_OPTS% %MAVEN_JAVA_OPTS%"

:javaOpts
@REM Add JVM options for Java
if not "%JAVA_OPTS%" == "" goto runMaven
set "MAVEN_OPTS=%MAVEN_OPTS% %JAVA_OPTS%"

:runMaven
"%MAVEN_JAVA_EXE%" %MAVEN_OPTS% -classpath "%WRAPPER_JAR%" "-Dmaven.home=%MAVEN_HOME%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" "-Dmaven.wrapper.version=3.1.1" "-Dmaven.repo.local=%USERPROFILE%\.m2\repository" org.apache.maven.wrapper.MavenWrapperMain %MAVEN_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

exit /B %ERROR_CODE%
