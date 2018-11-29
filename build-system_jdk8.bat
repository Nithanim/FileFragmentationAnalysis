set MINGW=%~dp0%mingw64\bin
set PATH=%MINGW%;%P%
cd native
call ..\mvnw install
cd ..
cd common
call ..\mvnw install
cd ..
cd assembly
call ..\mvnw package
