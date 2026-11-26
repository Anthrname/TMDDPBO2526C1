/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.model;

import ecowarrior.config.GameConfig;
import ecowarrior.entity.Bunker;
import ecowarrior.entity.BunkerType;
import ecowarrior.entity.Destroyer;
import ecowarrior.entity.EcoWarrior;
import ecowarrior.entity.Fire;
import ecowarrior.entity.Particle;
import ecowarrior.entity.WaterProjectile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Kelas GameState menyimpan seluruh keadaan (state) dunia game secara terpusat dalam modul Model.
 * Menyimpan daftar entitas aktif, skor sesi, kesehatan hutan, dan status game loop.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class GameState {

    private final String username;
    private final Achievement baseAchievement;
    private final RankTier rankTier;

    // Statistik Sesi Berjalan
    private int sessionDestroyer = 0;
    private int sessionFire = 0;
    private int sessionScore = 0;
    private double forestHealth = GameConfig.INITIAL_FOREST_HEALTH;
    private int combo = 0;
    private double comboTimer = 0;

    // Status Game
    private boolean paused = false;
    private boolean gameOver = false;
    private boolean victory = false;

    // Entitas Dunia Game
    private Bunker bunkerDestroyer;
    private Bunker bunkerEcoWarrior;
    private final List<EcoWarrior> ecoWarriors = Collections.synchronizedList(new ArrayList<>());
    private final List<Destroyer> destroyers = Collections.synchronizedList(new ArrayList<>());
    private final List<Fire> fires = Collections.synchronizedList(new ArrayList<>());
    private final List<WaterProjectile> waterProjectiles = Collections.synchronizedList(new ArrayList<>());
    private final List<Particle> particles = Collections.synchronizedList(new ArrayList<>());

    public GameState(String username, Achievement baseAchievement) {
        this.username = username;
        this.baseAchievement = (baseAchievement != null) ? baseAchievement : new Achievement(username, 0, 0, 0);
        this.rankTier = this.baseAchievement.getRankTier();
        initWorld();
    }

    private void initWorld() {
        // Bunker Destroyer diletakkan di sudut kiri bawah
        // Sesuai gambar spesifikasi halaman 7
        bunkerDestroyer = new Bunker(30, GameConfig.WINDOW_HEIGHT - 170, BunkerType.DESTROYER, 4.5);

        // Bunker Eco Warrior diletakkan di sudut kanan bawah
        // Sesuai gambar spesifikasi halaman 7
        bunkerEcoWarrior = new Bunker(GameConfig.WINDOW_WIDTH - 150, GameConfig.WINDOW_HEIGHT - 170, BunkerType.ECO_WARRIOR, 5.5);

        // Inisialisasi awal 2 Eco Warrior pejuang
        ecoWarriors.add(new EcoWarrior(bunkerEcoWarrior.getSpawnPointX() - 40, bunkerEcoWarrior.getSpawnPointY() - 30, rankTier));
        ecoWarriors.add(new EcoWarrior(bunkerEcoWarrior.getSpawnPointX() - 100, bunkerEcoWarrior.getSpawnPointY() - 70, rankTier));

        // Inisialisasi awal 2 Destroyer perusak hutan
        destroyers.add(new Destroyer(bunkerDestroyer.getSpawnPointX() + 40, bunkerDestroyer.getSpawnPointY() - 30));
        destroyers.add(new Destroyer(bunkerDestroyer.getSpawnPointX() + 90, bunkerDestroyer.getSpawnPointY() - 70));

        // Inisialisasi 1 titik api awal
        fires.add(new Fire(GameConfig.WINDOW_WIDTH / 2.0 - 50, GameConfig.WINDOW_HEIGHT / 2.0 - 60));
    }

    public void addScore(int points) {
        int comboBonus = (combo > 1) ? (combo * 5) : 0;
        this.sessionScore += (points + comboBonus);
    }

    public void registerExtinguishedFire() {
        this.sessionFire++;
        this.combo++;
        this.comboTimer = 3.5;
        addScore(GameConfig.SCORE_PER_FIRE);
    }

    public void registerNeutralizedDestroyer() {
        this.sessionDestroyer++;
        this.combo++;
        this.comboTimer = 3.5;
        addScore(GameConfig.SCORE_PER_DESTROYER);
    }

    public void updateCombo(double dt) {
        if (comboTimer > 0) {
            comboTimer -= dt;
            if (comboTimer <= 0) {
                combo = 0;
            }
        }
    }

    public String getUsername() { return username; }
    public Achievement getBaseAchievement() { return baseAchievement; }
    public RankTier getRankTier() { return rankTier; }

    public int getSessionDestroyer() { return sessionDestroyer; }
    public int getSessionFire() { return sessionFire; }
    public int getSessionScore() { return sessionScore; }
    public int getTotalAchievementScore() {
        return baseAchievement.getAchievement() + sessionScore;
    }

    public double getForestHealth() { return forestHealth; }
    public void setForestHealth(double forestHealth) {
        this.forestHealth = Math.max(0.0, Math.min(100.0, forestHealth));
        if (this.forestHealth <= 0) {
            this.gameOver = true;
            this.victory = false;
        }
    }

    public int getCombo() { return combo; }
    public boolean isPaused() { return paused; }
    public void setPaused(boolean paused) { this.paused = paused; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public boolean isVictory() { return victory; }
    public void setVictory(boolean victory) { this.victory = victory; }

    public Bunker getBunkerDestroyer() { return bunkerDestroyer; }
    public Bunker getBunkerEcoWarrior() { return bunkerEcoWarrior; }

    public List<EcoWarrior> getEcoWarriors() { return ecoWarriors; }
    public List<Destroyer> getDestroyers() { return destroyers; }
    public List<Fire> getFires() { return fires; }
    public List<WaterProjectile> getWaterProjectiles() { return waterProjectiles; }
    public List<Particle> getParticles() { return particles; }
}

