@echo off
chcp 65001 >nul
cd /d "%~dp0.."
if not exist out mkdir out
javac -d out src\*.java

echo === Путь к конфигу не передан ===
java -cp out Main
echo.
echo === Файл конфигурации не найден ===
java -cp out Main tests\configs\nope.xml
echo.
echo === Некорректный XML ===
java -cp out Main tests\configs\error-bad-xml.xml
echo.
echo === Нет параметра max-depth ===
java -cp out Main tests\configs\error-missing-tag.xml
echo.
echo === package: пробел в имени ===
java -cp out Main tests\configs\error-package.xml
echo.
echo === version: недопустимый символ ===
java -cp out Main tests\configs\error-version.xml
echo.
echo === repository: не URL в обычном режиме ===
java -cp out Main tests\configs\error-repository-url.xml
echo.
echo === repository: файла нет в тестовом режиме ===
java -cp out Main tests\configs\error-repository-file.xml
echo.
echo === test-mode: не true/false ===
java -cp out Main tests\configs\error-test-mode.xml
echo.
echo === output-file: не .png ===
java -cp out Main tests\configs\error-output-file.xml
echo.
echo === ascii-tree: не true/false ===
java -cp out Main tests\configs\error-ascii-tree.xml
echo.
echo === max-depth: отрицательное число ===
java -cp out Main tests\configs\error-depth-negative.xml
echo.
echo === max-depth: не число ===
java -cp out Main tests\configs\error-depth-text.xml
echo.
echo === filter: есть пробел ===
java -cp out Main tests\configs\error-filter.xml
echo.