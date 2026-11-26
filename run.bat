@echo off
chcp 65001 >nul
title ECO WARRIOR - Launcher

echo =======================================================
echo   ECO WARRIOR - Forest Protection Simulator
echo   Mahasiswa : Najib Nurohman (NIM: 2509653) - Kelas C1
echo   Dosen     : Rosa Ariani Sukamto
echo   DPBO 2026 - Ilmu Komputer FPMIPA UPI
echo =======================================================
echo.

REM Pastikan Java tersedia
where java >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java tidak ditemukan! Harap install Java 17+ terlebih dahulu.
    pause
    exit /b 1
)

echo [INFO] Memulai ECO WARRIOR...
java -cp "EcoWarrior.jar;lib\mysql-connector-j-8.3.0.jar;lib\sqlite-jdbc-3.43.2.2.jar;lib\slf4j-api-2.0.9.jar;lib\slf4j-nop-2.0.9.jar" Main

if errorlevel 1 (
    echo.
    echo [INFO] Mencoba langsung dari out/...
    java -cp "out;lib\mysql-connector-j-8.3.0.jar;lib\sqlite-jdbc-3.43.2.2.jar;lib\slf4j-api-2.0.9.jar;lib\slf4j-nop-2.0.9.jar" Main
)

pause
