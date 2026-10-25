@echo off
chcp 65001 >nul
cd /d "%~dp0.."
if not exist out mkdir out
javac -d out src\*.java

echo === Нормальный конфиг ===
java -cp out Main tests\configs\ok.xml
echo.
echo === Тестовый режим (путь к существующему файлу) ===
java -cp out Main tests\configs\ok-test-mode.xml
echo.