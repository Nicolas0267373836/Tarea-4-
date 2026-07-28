@echo off
setlocal
cd /d "%~dp0"

if not exist "target\sistema-usuarios-tarea4.jar" (
    echo El programa aun no esta compilado.
    echo Ejecuta primero compilar_y_ejecutar.bat
    pause
    exit /b 1
)

java -jar target\sistema-usuarios-tarea4.jar
if errorlevel 1 pause

endlocal

