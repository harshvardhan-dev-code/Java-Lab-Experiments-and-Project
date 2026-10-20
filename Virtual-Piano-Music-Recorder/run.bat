@echo off
REM Compiles and runs the Virtual Piano on Windows
cd /d "%~dp0"
javac -d out -sourcepath src src\piano\Main.java
if errorlevel 1 (
    echo Compilation failed.
    pause
    exit /b 1
)
java -cp out piano.Main
