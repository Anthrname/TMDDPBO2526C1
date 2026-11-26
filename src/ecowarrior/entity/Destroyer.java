/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.entity;

import ecowarrior.config.GameConfig;
import ecowarrior.assets.Atlas;
import ecowarrior.model.GameState;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Entitas Destroyer: Pion perusak hutan yang membakar lahan demi uang.
 * Dihasilkan oleh Bunker Destroyer. Bergerak menyusuri hutan dan menyalakan api.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class Destroyer extends GameObject {

    private double health = 100.0;
    private final double maxHealth = 100.0;
    private double fireCooldown = 4.0 + Math.random() * 4.0;
    private double targetMoveX;
    private double targetMoveY;
    private double changeTargetTimer = 0;
    private double slowTimer = 0;
    private boolean wantsToPlantFire = false;

    public Destroyer(double startX, double startY) {
        super(startX, startY, 34, 34);
        pickNewDestination();
    }

    private void pickNewDestination() {
        // Pilih koordinat target di area tengah-atas hutan
        this.targetMoveX = 80 + Math.random() * (GameConfig.WINDOW_WIDTH - 200);
        this.targetMoveY = 100 + Math.random() * (GameConfig.WINDOW_HEIGHT - 260);
        this.changeTargetTimer = 3.0 + Math.random() * 4.0;
    }

    @Override
    public void update(double dt) {
        changeTargetTimer -= dt;
        if (changeTargetTimer <= 0 || distanceTo(targetMoveX, targetMoveY) < 20) {
            pickNewDestination();
        }

        // Hitung pergerakan menuju targetMove
        double angle = Math.atan2(targetMoveY - getCenterY(), targetMoveX - getCenterX());
        double currentSpeed = GameConfig.BASE_DESTROYER_SPEED;

        // Efek basah terkena air memperlambat gerak (slow debuff)
        if (slowTimer > 0) {
            slowTimer -= dt;
            currentSpeed *= 0.55;
        }

        vx = Math.cos(angle) * currentSpeed;
        vy = Math.sin(angle) * currentSpeed;

        x += vx;
        y += vy;

        // Manajemen pembakaran api
        fireCooldown -= dt;
        if (fireCooldown <= 0) {
            wantsToPlantFire = true;
            fireCooldown = 6.0 + Math.random() * 5.0; // Cooldown menyalakan api berikutnya
        }
    }

    public boolean consumePlantFireRequest() {
        if (wantsToPlantFire) {
            wantsToPlantFire = false;
            return true;
        }
        return false;
    }

    public void takeDamage(double dmg) {
        this.health -= dmg;
        this.slowTimer = 1.2;
        if (this.health <= 0) {
            this.health = 0;
            this.active = false;
        }
    }

    public double getHealth() {
        return health;
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!active) return;

        // Gambar karakter dari atlas (animasi idle / jalan).
        // Kunci: "dest_hero_anim" (registrasi di GameAssets.build)
        Atlas.INSTANCE.drawScreen(g2d, "dest_hero_anim",
                x, y, 0, (int) width, (int) height, (long) GameState.getGameTime());

        // Bar darah/ketahanan di atas kepala jika terluka
        if (health < maxHealth) {
            int d = (int) width;
            int barW = d;
            int barH = 4;
            int barY = (int) (y - 8);
            g2d.setColor(new Color(50, 50, 50, 180));
            g2d.fillRect((int) x, barY, barW, barH);
            g2d.setColor(new Color(231, 76, 60));
            int currW = (int) ((health / maxHealth) * barW);
            g2d.fillRect((int) x, barY, currW, barH);
            g2d.setColor(Color.WHITE);
            g2d.drawRect((int) x, barY, barW, barH);
        }
    }
}

