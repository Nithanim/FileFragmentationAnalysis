set P=%PATH%
set MINGW=%~dp0%mingw64\bin

set JAVA_HOME=%~dp0%jdk8
set PATH=%JAVA_HOME%\bin;%MINGW%;%P%
cd native
call ..\mvnw clean install
cd ..

set JAVA_HOME=%~dp0%jdk11
set PATH=%JAVA_HOME%\bin;%P%
cd common
call ..\mvnw clean install
cd ..
cd assembly
call ..\mvnw clean package

set PATH=%P%