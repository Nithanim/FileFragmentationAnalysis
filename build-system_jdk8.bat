set MINGW=%~dp0%mingw64\bin
set PATH=%MINGW%;%P%
cd native
call ..\mvnw clean install
cd ..
cd common
call ..\mvnw clean install
cd ..
cd assembly
call ..\mvnw clean package
