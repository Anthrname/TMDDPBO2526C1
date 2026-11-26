/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.presenter;

import ecowarrior.audio.SoundManager;
import ecowarrior.config.GameConfig;
import ecowarrior.entity.Bunker;
import ecowarrior.entity.Destroyer;
import ecowarrior.entity.EcoWarrior;
import ecowarrior.entity.Fire;
import ecowarrior.entity.GameObject;
import ecowarrior.entity.Particle;
import ecowarrior.entity.WaterProjectile;
import ecowarrior.model.Achievement;
import ecowarrior.model.AchievementDAO;
import ecowarrior.model.GameState;
import ecowarrior.view.GameFrame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * GamePresenter - Lapisan Logika Permainan (Presenter Layer - Pola MVP).
 * 
 * Bertugas menjalankan Game Loop utama (Thread 60 FPS) yang mencakup:
 * 1. Update seluruh entitas (Bunker, Destroyer, EcoWarrior, Fire, Projectile, Particle).
 * 2. Mendeteksi seluruh tumbukan/collision (Water vs Destroyer, Water vs Fire, Destroyer vs Forest).
 * 3. Menangani input klik mouse (menugaskan Eco Warrior terdekat ke target).
 * 4. Menyimpan statistik sesi ke database saat game berakhir atau pemain berhenti.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class GamePresenter implements Runnable {

    private final GameFrame view;
    private final AchievementDAO dao;
    private final GameState gameState;
    private final Runnable onGameOver;
    private final Random rng = new Random();

    private volatile boolean running = true;
    private Thread gameThread;

    public GamePresenter(GameFrame view, AchievementDAO dao, GameState gameState, Runnable onGameOver) {
        this.view = view;
        this.dao = dao;
        this.gameState = gameState;
        this.onGameOver = onGameOver;

        view.setMouseClickHandler(this::handleMouseClick);
        view.setOnPauseToggle(this::togglePause);
        view.setOnMuteToggle(() -> SoundManager.getInstance().toggleMute());
        view.setOnSaveAndQuit(this::saveAndQuit);
    }

    public void start() {
        SoundManager.getInstance().playGameBGM();
        gameThread = new Thread(this, "GameLoopThread");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            double dt = (now - lastTime) / 1_000_000_000.0;
            lastTime = now;

            // Clamp delta time agar tidak melambung saat jeda sistem
            dt = Math.min(dt, 0.05);

            if (!gameState.isPaused() && !gameState.isGameOver()) {
                update(dt);
            }

            view.renderState(gameState);

            if (gameState.isGameOver()) {
                running = false;
                triggerGameOver();
                return;
            }

            // Kelola sisa waktu frame agar tetap di 60 FPS
            long elapsed = System.nanoTime() - now;
            long sleepMs = (GameConfig.TARGET_TIME) - (elapsed / 1_000_000);
            if (sleepMs > 0) {
                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // ===================================================================
    // UPDATE: Logika inti Game Loop
    // ===================================================================

    private void update(double dt) {
        gameState.updateCombo(dt);

        updateBunkers(dt);
        updateDestroyers(dt);
        updateEcoWarriors(dt);
        updateProjectiles(dt);
        updateFires(dt);
        updateParticles(dt);
        resolveCollisions();
        applyFireForestDamage(dt);
    }

    private void updateBunkers(double dt) {
        // Update Bunker Destroyer — Hasilkan Destroyer baru
        Bunker bd = gameState.getBunkerDestroyer();
        bd.update(dt);
        List<Destroyer> destroyers = gameState.getDestroyers();
        if (bd.canSpawnUnit(destroyers.size(), GameConfig.MAX_DESTROYERS)) {
            Destroyer newDest = new Destroyer(bd.getSpawnPointX(), bd.getSpawnPointY());
            synchronized (destroyers) { destroyers.add(newDest); }
        }

        // Update Bunker Eco Warrior — Hasilkan EcoWarrior baru
        Bunker bw = gameState.getBunkerEcoWarrior();
        bw.update(dt);
        List<EcoWarrior> warriors = gameState.getEcoWarriors();
        if (bw.canSpawnUnit(warriors.size(), GameConfig.MAX_ECO_WARRIORS)) {
            EcoWarrior newWarrior = new EcoWarrior(bw.getSpawnPointX(), bw.getSpawnPointY(), gameState.getRankTier());
            synchronized (warriors) { warriors.add(newWarrior); }
        }
    }

    private void updateDestroyers(double dt) {
        List<Destroyer> destroyers = gameState.getDestroyers();
        List<Fire> fires = gameState.getFires();

        synchronized (destroyers) {
            for (Destroyer d : destroyers) {
                if (!d.isActive()) continue;
                d.update(dt);

                // Jika Destroyer memutuskan menyalakan api, tambah titik api baru
                if (d.consumePlantFireRequest()) {
                    int fireCount;
                    synchronized (fires) { fireCount = fires.size(); }
                    if (fireCount < GameConfig.MAX_FIRES) {
                        double fx = d.getCenterX() + (rng.nextDouble() - 0.5) * 60;
                        double fy = d.getCenterY() + (rng.nextDouble() - 0.5) * 60;
                        fx = Math.max(50, Math.min(GameConfig.WINDOW_WIDTH - 80, fx));
                        fy = Math.max(130, Math.min(GameConfig.WINDOW_HEIGHT - 100, fy));
                        synchronized (fires) { fires.add(new Fire(fx, fy)); }
                    }
                }
            }
            destroyers.removeIf(d -> !d.isActive());
        }
    }

    private void updateEcoWarriors(double dt) {
        List<EcoWarrior> warriors = gameState.getEcoWarriors();
        List<WaterProjectile> projectiles = gameState.getWaterProjectiles();

        synchronized (warriors) {
            for (EcoWarrior w : warriors) {
                if (!w.isActive()) continue;
                w.update(dt);

                // Coba tembakkan air ke target
                WaterProjectile proj = w.attemptShootWater();
                if (proj != null) {
                    synchronized (projectiles) { projectiles.add(proj); }
                    // Hanya putar SFX sesekali agar tidak berisik
                    if (rng.nextFloat() < 0.3f) {
                        SoundManager.getInstance().playWaterShoot();
                    }
                }
            }
        }
    }

    private void updateProjectiles(double dt) {
        List<WaterProjectile> projectiles = gameState.getWaterProjectiles();
        synchronized (projectiles) {
            for (WaterProjectile p : projectiles) {
                if (p.isActive()) p.update(dt);
            }
            projectiles.removeIf(p -> !p.isActive());
        }
    }

    private void updateFires(double dt) {
        List<Fire> fires = gameState.getFires();
        List<Particle> particles = gameState.getParticles();
        synchronized (fires) {
            for (Fire f : fires) {
                if (!f.isActive()) continue;
                f.update(dt);

                // Hasilkan partikel bara api (ember)
                if (f.shouldSpawnEmber()) {
                    double ex = f.getCenterX() + (rng.nextDouble() - 0.5) * 20;
                    double ey = f.getY() + (rng.nextDouble()) * 10;
                    Particle ember = new Particle(ex, ey,
                            (rng.nextDouble() - 0.5) * 2.0,
                            -1.5 - rng.nextDouble() * 1.5,
                            4 + rng.nextInt(5),
                            new java.awt.Color(255, 90 + rng.nextInt(100), 20),
                            0.6f + rng.nextFloat() * 0.5f,
                            Particle.Type.EMBER
                    );
                    synchronized (particles) { particles.add(ember); }
                }
            }
            fires.removeIf(f -> !f.isActive());
        }
    }

    private void updateParticles(double dt) {
        List<Particle> particles = gameState.getParticles();
        synchronized (particles) {
            for (Particle p : particles) {
                if (p.isActive()) p.update(dt);
            }
            // Bersihkan partikel yang sudah mati agar tidak terjadi memory leak
            if (particles.size() > 300) {
                particles.removeIf(p -> !p.isActive());
            }
        }
    }

    // ===================================================================
    // COLLISION DETECTION
    // ===================================================================

    private void resolveCollisions() {
        List<WaterProjectile> projectiles = gameState.getWaterProjectiles();
        List<Destroyer> destroyers = gameState.getDestroyers();
        List<Fire> fires = gameState.getFires();
        List<Particle> particles = gameState.getParticles();

        synchronized (projectiles) {
            for (WaterProjectile proj : projectiles) {
                if (!proj.isActive()) continue;

                // Cek tumbukan proyektil air dengan Destroyer
                synchronized (destroyers) {
                    for (Destroyer d : destroyers) {
                        if (!d.isActive()) continue;
                        if (proj.intersects(d)) {
                            d.takeDamage(proj.getDamage());
                            proj.setActive(false);

                            spawnWaterSplashParticles(proj.getCenterX(), proj.getCenterY(), particles);

                            if (!d.isActive()) {
                                // Destroyer berhasil dilumpuhkan!
                                gameState.registerNeutralizedDestroyer();
                                SoundManager.getInstance().playDestroyerHit();
                                spawnFloatingText(d.getCenterX(), d.getY() - 10, "+50!", particles);
                            }
                            break;
                        }
                    }
                }

                if (!proj.isActive()) continue;

                // Cek tumbukan proyektil air dengan Titik Api
                synchronized (fires) {
                    for (Fire f : fires) {
                        if (!f.isActive()) continue;
                        if (proj.intersects(f)) {
                            double extinguishPower = proj.getDamage();
                            f.extinguish(extinguishPower);
                            proj.setActive(false);

                            spawnSteamParticles(f.getCenterX(), f.getCenterY(), particles);

                            // Splash Damage: padamkan api terdekat juga (Skill rank tinggi)
                            if (proj.isSplash()) {
                                for (Fire nearFire : fires) {
                                    if (nearFire != f && nearFire.isActive() && proj.distanceTo(nearFire) < 100) {
                                        nearFire.extinguish(extinguishPower * 0.5);
                                    }
                                }
                            }

                            if (!f.isActive()) {
                                // Titik Api berhasil dipadamkan!
                                gameState.registerExtinguishedFire();
                                SoundManager.getInstance().playExtinguish();
                                spawnFloatingText(f.getCenterX(), f.getY() - 10, "+25!", particles);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private void applyFireForestDamage(double dt) {
        List<Fire> fires = gameState.getFires();
        int activeFireCount;
        synchronized (fires) {
            activeFireCount = (int) fires.stream().filter(Fire::isActive).count();
        }

        if (activeFireCount > 0) {
            double damage = activeFireCount * GameConfig.FIRE_HEALTH_DECAY_RATE * dt;
            gameState.setForestHealth(gameState.getForestHealth() - damage);
        } else if (gameState.getRankTier().isRegenUnlocked()) {
            // Earth Legend: Regen hutan jika tidak ada api
            gameState.setForestHealth(gameState.getForestHealth() + 1.5 * dt);
        }
    }

    // ===================================================================
    // PARTIKEL EFEK VISUAL
    // ===================================================================

    private void spawnWaterSplashParticles(double x, double y, List<Particle> particles) {
        for (int i = 0; i < 8; i++) {
            double angle = Math.PI * 2 * i / 8;
            Particle p = new Particle(x, y,
                    Math.cos(angle) * (1.5 + rng.nextDouble() * 2.5),
                    Math.sin(angle) * (1.5 + rng.nextDouble() * 2.5),
                    5 + rng.nextInt(5),
                    new java.awt.Color(41, 128, 185),
                    0.5f,
                    Particle.Type.WATER
            );
            synchronized (particles) { particles.add(p); }
        }
    }

    private void spawnSteamParticles(double x, double y, List<Particle> particles) {
        for (int i = 0; i < 6; i++) {
            Particle p = new Particle(x + (rng.nextDouble() - 0.5) * 24, y,
                    (rng.nextDouble() - 0.5) * 1.2,
                    -1.5 - rng.nextDouble() * 2,
                    10 + rng.nextInt(10),
                    new java.awt.Color(190, 210, 230, 160),
                    0.9f,
                    Particle.Type.SMOKE
            );
            synchronized (particles) { particles.add(p); }
        }
    }

    private void spawnFloatingText(double x, double y, String text, List<Particle> particles) {
        Particle p = Particle.createFloatingText(x - 15, y, text, new java.awt.Color(241, 196, 15));
        synchronized (particles) { particles.add(p); }
    }

    // ===================================================================
    // INPUT MOUSE: Eco Warrior terdekat menerima target
    // ===================================================================

    public void handleMouseClick(int clickX, int clickY) {
        if (gameState.isPaused() || gameState.isGameOver()) return;

        // 1. Cek apakah klik mengenai Destroyer
        List<Destroyer> destroyers = gameState.getDestroyers();
        GameObject clickedTarget = null;
        double minDist = Double.MAX_VALUE;

        synchronized (destroyers) {
            for (Destroyer d : destroyers) {
                if (!d.isActive()) continue;
                double dist = d.distanceTo(clickX, clickY);
                if (dist < 50 && dist < minDist) {
                    minDist = dist;
                    clickedTarget = d;
                }
            }
        }

        // 2. Cek apakah klik mengenai Titik Api
        List<Fire> fires = gameState.getFires();
        synchronized (fires) {
            for (Fire f : fires) {
                if (!f.isActive()) continue;
                double dist = f.distanceTo(clickX, clickY);
                if (dist < 50 && dist < minDist) {
                    minDist = dist;
                    clickedTarget = f;
                }
            }
        }

        // 3. Tugaskan Eco Warrior terdekat untuk menangani target klik
        List<EcoWarrior> warriors = gameState.getEcoWarriors();
        EcoWarrior closestWarrior = null;
        double closestWarriorDist = Double.MAX_VALUE;

        synchronized (warriors) {
            for (EcoWarrior w : warriors) {
                if (!w.isActive()) continue;
                double dist = w.distanceTo(clickX, clickY);
                if (dist < closestWarriorDist) {
                    closestWarriorDist = dist;
                    closestWarrior = w;
                }
            }
        }

        if (closestWarrior != null) {
            if (clickedTarget != null) {
                closestWarrior.assignTarget(clickedTarget);
            } else {
                closestWarrior.assignMoveTarget(clickX, clickY);
            }
        }
    }

    // ===================================================================
    // KONTROL GAME: Pause, Mute, Simpan & Keluar
    // ===================================================================

    public void togglePause() {
        gameState.setPaused(!gameState.isPaused());
        SoundManager.getInstance().playClick();
    }

    public void saveAndQuit() {
        if (!running) return;
        running = false;

        // 1. Simpan statistik sesi ke database tachievement secara permanen
        dao.saveSessionStats(
                gameState.getUsername(),
                gameState.getSessionDestroyer(),
                gameState.getSessionFire(),
                gameState.getSessionScore()
        );
        SoundManager.getInstance().stopBGM();

        // 2. Langsung kembali ke tampilan awal (Menu Utama) sesuai spesifikasi soal halaman 7:
        // "Tombol space digunakan untuk menghentikan permainan dan kembali pada tampilan awal."
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (onGameOver != null) {
                onGameOver.run();
            } else {
                view.closeGame();
            }
        });
    }

    private void triggerGameOver() {
        // Simpan statistik terakhir ke database
        dao.saveSessionStats(
                gameState.getUsername(),
                gameState.getSessionDestroyer(),
                gameState.getSessionFire(),
                gameState.getSessionScore()
        );
        SoundManager.getInstance().stopBGM();

        javax.swing.SwingUtilities.invokeLater(() -> {
            view.showGameOverDialogFull(
                    gameState.getUsername(),
                    gameState.getSessionScore(),
                    gameState.getSessionDestroyer(),
                    gameState.getSessionFire(),
                    gameState.getTotalAchievementScore(),
                    gameState.isVictory()
            );
        });
    }

    public void stop() {
        running = false;
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }
}
