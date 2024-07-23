rem Comandos para fazer o deploy
rem SETLOCAL enabledelayedexpansion

cls
set targetFolder=C:\tmp\ToGo\Tomcat10\webapps
echo Target = %targetFolder%

echo Copiando WAR.
del %targetFolder%\target\webBot*.*
copy ..\target\webBot*.war %targetFolder%\

pause
