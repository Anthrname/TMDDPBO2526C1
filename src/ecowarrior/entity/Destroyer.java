/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.entity;

import ecowarrior.config.GameConfig;
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

        int ix = (int) x;
        int iy = (int) y;
        int d = (int) width;

        // Bayangan di tanah
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillOval(ix + 2, iy + d - 4, d - 4, 10);

        // Lingkaran Karakter Merah (Sesuai diagram spesifikasi halaman 7)
        g2d.setColor(new Color(231, 76, 60));
        g2d.fillOval(ix, iy, d, d);

        // Border luar lingkaran merah pekat
        g2d.setColor(new Color(192, 57, 43));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawOval(ix, iy, d, d);

        // Mata jahat (Kacamata asap / tatapan licik)
        g2d.setColor(Color.BLACK);
        g2d.fillOval(ix + 8, iy + 10, 6, 8);
        g2d.fillOval(ix + 20, iy + 10, 6, 8);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(ix + 9, iy + 11, 2, 2);
        g2d.fillOval(ix + 21, iy + 11, 2, 2);

        // Senyum menyeringai licik
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.8f));
        g2d.drawArc(ix + 9, iy + 18, 16, 8, 0, -180);

        // Indikator status jika terkena siraman air (Slow / Soaked)
        if (slowTimer > 0) {
            g2d.setColor(new Color(52, 152, 219, 180));
            g2d.fillOval(ix + 4, iy - 6, 8, 8);
            g2d.fillOval(ix + 20, iy - 8, 6, 6);
        }

        // Bar darah/ketahanan di atas kepala jika terluka
        if (health < maxHealth) {
            int barW = d;
            int barH = 4;
            int barY = iy - 8;
            g2d.setColor(new Color(50, 50, 50, 180));
            g2d.fillRect(ix, barY, barW, barH);
            g2d.setColor(new Color(231, 76, 60));
            int currW = (int) ((health / maxHealth) * barW);
            g2d.fillRect(ix, barY, currW, barH);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(ix, barY, barW, barH);
        }
    }
}

