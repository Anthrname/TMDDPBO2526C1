/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.entity;

import ecowarrior.assets.Atlas;
import ecowarrior.model.GameState;
import ecowarrior.config.GameConfig;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Entitas Proyektil Air (Water Spray / Water Bomb) yang ditembakkan oleh Eco Warrior
 * ke arah destroyer ataupun titik api yang ditentukan.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class WaterProjectile extends GameObject {

    private final double startX;
    private final double startY;
    private final double targetX;
    private final double targetY;
    private final double damage;
    private final boolean splash;
    private final GameObject targetEntity;
    private double lifeTimer = 0;
    private final double maxLifeTime = 2.0;
    private final double totalDistance;
    private final double maxArcHeight;

    public WaterProjectile(double startX, double startY, double targetX, double targetY, 
                           double damage, boolean splash, GameObject targetEntity) {
        super(startX, startY, 14, 14);
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.damage = damage;
        this.splash = splash;
        this.targetEntity = targetEntity;

        this.totalDistance = Math.hypot(targetX - startX, targetY - startY);
        this.maxArcHeight = Math.min(120.0, 30.0 + totalDistance * 0.22);
        this.z = 10;

        double angle = Math.atan2(targetY - startY, targetX - startX);
        this.vx = Math.cos(angle) * GameConfig.PROJECTILE_SPEED;
        this.vy = Math.sin(angle) * GameConfig.PROJECTILE_SPEED;
    }

    @Override
    public void update(double dt) {
        x += vx;
        y += vy;
        lifeTimer += dt;

        // Hitung lintasan parabola 3D balistik di udara
        double distTraveled = Math.hypot(x - startX, y - startY);
        double progress = Math.min(1.0, distTraveled / Math.max(1.0, totalDistance));
        this.z = Math.sin(progress * Math.PI) * maxArcHeight;

        if (lifeTimer >= maxLifeTime) {
            active = false;
        }

        // Cek apakah sudah sangat dekat dengan target
        if (distanceTo(targetX, targetY) < 16) {
            active = false;
        }
    }

    public double getDamage() {
        return damage;
    }

    public boolean isSplash() {
        return splash;
    }

    public GameObject getTargetEntity() {
        return targetEntity;
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!active) return;

        // 1. Gambar bayangan proyektil di permukaan tanah 3D (Z = 0)
        double shadowScale = Math.max(0.4, 1.0 - (z / (maxArcHeight + 1.0)) * 0.5);
        int sw = (int) (width * shadowScale);
        int sh = (int) (height * 0.5 * shadowScale);
        g2d.setColor(new Color(10, 25, 20, 100));
        g2d.fillOval((int) (x + width / 2.0 - sw / 2.0), (int) (y + height / 2.0 - sh / 2.0), sw, sh);

        // 2. Gambar bola air proyektil dari atlas (animasi)
        // Kunci: "water_shot" (registrasi di GameAssets.build)
        Atlas.INSTANCE.drawScreen(g2d, "water_shot",
                x, y, z, (int) width, (int) height, (long) GameState.getGameTime());
    }
}

