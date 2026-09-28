@echo off
setlocal
cd /d "%~dp0"

if not defined JAVA_HOME set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"
if not defined MAVEN_HOME set "MAVEN_HOME=C:\Program Files\apache-maven-3.9.11"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"

echo JAVA_HOME=%JAVA_HOME%
echo MAVEN_HOME=%MAVEN_HOME%
echo.

if "%~1"=="" goto run
if /I "%~1"=="compile" goto compile
if /I "%~1"=="run" goto run
if /I "%~1"=="version" goto version

echo Uso:
echo   run.bat           compila y ejecuta (exec:java)
echo   run.bat compile   solo compila
echo   run.bat version   muestra mvn -v
exit /b 1

:version
call mvn -v
exit /b %ERRORLEVEL%

:compile
call mvn -DskipTests compile
exit /b %ERRORLEVEL%

:run
call mvn -DskipTests compile exec:java
exit /b %ERRORLEVEL%
