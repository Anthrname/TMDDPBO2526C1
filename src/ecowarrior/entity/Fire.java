/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.entity;

import ecowarrior.assets.Atlas;
import ecowarrior.model.GameState;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

/**
 * Entitas Titik Api (Fire) yang dihasilkan oleh Destroyer di lahan hutan.
 * Memiliki animasi api bergelombang dinamis (flickering flame) dan bar ketahanan api.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class Fire extends GameObject {
    private double health = 100.0;
    private final double maxHealth = 100.0;
    private double animTime = Math.random() * 10.0;
    private double emberTimer = 0.0;

    public Fire(double x, double y) {
        super(x, y, 38, 46);
    }

    @Override
    public void update(double dt) {
        animTime += dt * 8.0;
        emberTimer += dt;
    }

    public boolean shouldSpawnEmber() {
        if (emberTimer > 0.15) {
            emberTimer = 0;
            return true;
        }
        return false;
    }

    public void extinguish(double damage) {
        this.health -= damage;
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

        double flicker = Math.sin(animTime) * 4.0;
        int bx = (int) x;
        int by = (int) y;
        int w = (int) width;
        int h = (int) (height + flicker);

        // Bayangan bara api di tanah
        g2d.setColor(new Color(60, 20, 10, 140));
        g2d.fillOval(bx - 4, by + (int) height - 8, w + 8, 14);

        // Perlapan api (animasi ember) dari atlas
        // Kunci: "fire_hero_anim" (registrasi di GameAssets.build)
        Atlas.INSTANCE.drawScreen(g2d, "fire_hero_anim",
                x, y, 0, (int) width, (int) height, (long) GameState.getGameTime());

        // Bar Ketahanan Titik Api (Jika terluka)
        if (health < maxHealth) {
            int barW = (int) width;
            int barH = 5;
            int barY = by - 10;
            g2d.setColor(new Color(40, 40, 40, 180));
            g2d.fillRect(bx, barY, barW, barH);
            g2d.setColor(new Color(231, 76, 60));
            int currentW = (int) ((health / maxHealth) * barW);
            g2d.fillRect(bx, barY, currentW, barH);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(bx, barY, barW, barH);
        }
    }
}

