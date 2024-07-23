@echo ON
setlocal EnableDelayedExpansion

echo Clean
SET JAVA_HOME=C:\Program Files\Java\jdk-21.0.1

set Path=C:\Program Files\NetBeans-15\netbeans\java\maven\bin\mvn.cmd
set Command="%Path%" clean install -U -e -X
call %%Command%%
pause


rem mvn clean install
rem "%Path%" spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
rem pause
