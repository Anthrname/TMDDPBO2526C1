/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.entity;

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

    private final double targetX;
    private final double targetY;
    private final double damage;
    private final boolean splash;
    private final GameObject targetEntity;
    private double lifeTimer = 0;
    private final double maxLifeTime = 2.0;

    public WaterProjectile(double startX, double startY, double targetX, double targetY, 
                           double damage, boolean splash, GameObject targetEntity) {
        super(startX, startY, 14, 14);
        this.targetX = targetX;
        this.targetY = targetY;
        this.damage = damage;
        this.splash = splash;
        this.targetEntity = targetEntity;

        double angle = Math.atan2(targetY - startY, targetX - startX);
        this.vx = Math.cos(angle) * GameConfig.PROJECTILE_SPEED;
        this.vy = Math.sin(angle) * GameConfig.PROJECTILE_SPEED;
    }

    @Override
    public void update(double dt) {
        x += vx;
        y += vy;
        lifeTimer += dt;

        if (lifeTimer >= maxLifeTime) {
            active = false;
        }

        // Cek apakah sudah sangat dekat dengan target
        if (distanceTo(targetX, targetY) < 14) {
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

        // Visual bola air dengan efek kilau gradien biru cyan
        g2d.setColor(new Color(0, 180, 255, 120));
        g2d.fillOval((int) (x - 3), (int) (y - 3), (int) width + 6, (int) height + 6);

        g2d.setColor(new Color(41, 128, 185));
        g2d.fillOval((int) x, (int) y, (int) width, (int) height);

        g2d.setColor(new Color(174, 214, 241));
        g2d.fillOval((int) x + 2, (int) y + 2, (int) width - 6, (int) height - 6);
    }
}

