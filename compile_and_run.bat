@echo off
chcp 65001 >nul
title ECO WARRIOR - Compile ^& Run

echo [1/3] Membersihkan hasil kompilasi lama...
if exist out rmdir /s /q out
mkdir out

echo [2/3] Mengkompilasi seluruh kode sumber Java...
for /R src %%f in (*.java) do set SOURCES=!SOURCES! "%%f"

javac -encoding UTF-8 -d out -cp "src;lib\mysql-connector-j-8.3.0.jar;lib\sqlite-jdbc-3.45.1.0.jar" ^
  src\Main.java ^
  src\ecowarrior\config\GameConfig.java ^
  src\ecowarrior\model\Achievement.java ^
  src\ecowarrior\model\RankTier.java ^
  src\ecowarrior\model\DatabaseConnection.java ^
  src\ecowarrior\model\AchievementDAO.java ^
  src\ecowarrior\model\GameState.java ^
  src\ecowarrior\entity\GameObject.java ^
  src\ecowarrior\entity\BunkerType.java ^
  src\ecowarrior\entity\Particle.java ^
  src\ecowarrior\entity\Fire.java ^
  src\ecowarrior\entity\WaterProjectile.java ^
  src\ecowarrior\entity\Destroyer.java ^
  src\ecowarrior\entity\EcoWarrior.java ^
  src\ecowarrior\entity\Bunker.java ^
  src\ecowarrior\audio\AudioSynthesizer.java ^
  src\ecowarrior\audio\SoundManager.java ^
  src\ecowarrior\view\IMenuView.java ^
  src\ecowarrior\view\IGameView.java ^
  src\ecowarrior\view\components\ModernButton.java ^
  src\ecowarrior\view\components\ModernTable.java ^
  src\ecowarrior\view\components\SoundControlPanel.java ^
  src\ecowarrior\view\dialogs\AboutDialog.java ^
  src\ecowarrior\view\dialogs\HowToPlayDialog.java ^
  src\ecowarrior\view\dialogs\GameOverDialog.java ^
  src\ecowarrior\view\GamePanel.java ^
  src\ecowarrior\view\GameFrame.java ^
  src\ecowarrior\view\MenuFrame.java ^
  src\ecowarrior\presenter\MenuPresenter.java ^
  src\ecowarrior\presenter\GamePresenter.java

if errorlevel 1 (
    echo.
    echo [ERROR] Kompilasi GAGAL! Periksa kode sumber.
    pause
    exit /b 1
)

echo [3/3] Kompilasi berhasil! Memulai program...
echo.
java -cp "out;lib\mysql-connector-j-8.3.0.jar;lib\sqlite-jdbc-3.43.2.2.jar;lib\slf4j-api-2.0.9.jar;lib\slf4j-nop-2.0.9.jar" Main

pause
