@echo off
setlocal
cd /d "%~dp0"

where java >nul 2>&1
if errorlevel 1 (
    echo ERROR: No se encontro Java. Instala JDK 17 y vuelve a intentarlo.
    pause
    exit /b 1
)

where mvn >nul 2>&1
if errorlevel 1 (
    echo ERROR: No se encontro Maven. Instala Maven 3.9 o ejecuta desde Eclipse.
    pause
    exit /b 1
)

echo Compilando y ejecutando pruebas...
call mvn clean package
if errorlevel 1 (
    echo.
    echo La compilacion fallo. Revisa el mensaje anterior.
    pause
    exit /b 1
)

echo.
echo Iniciando Sistema de Usuarios...
java -jar target\sistema-usuarios-tarea4.jar

endlocal

