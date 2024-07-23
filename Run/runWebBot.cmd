rem @echo off

cd ..\evolution-api

tasklist /FI "IMAGENAME eq node.exe" 2>NUL | find /I /N "node.exe" >NUL
if "%ERRORLEVEL%"=="0" (
   echo Node.js is already running.
) else (
    start node .\dist\src\main.js
)

rem start node .\dist\src\main.js
rem start npm run start:prod

cd ..

rem SET JAVA_HOME="C:\Program Files\Java\jdk1.8.0_151"

SETLOCAL enabledelayedexpansion
for /r . %%g in (\target\WebBot-*.jar) do set s=%%g 
echo %s%

echo "Run java"
"%JAVA_HOME%"\bin\java -jar %s% -classpath .\target -Xmx2048m -Xms256m

pause

