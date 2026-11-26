/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Entitas Bunker: Markas penghasil unit.
 * Sesuai spesifikasi:
 * - Bunker Destroyer (kiri bawah): Menghasilkan pion Destroyer.
 * - Bunker Eco Warrior (kanan bawah): Menghasilkan pion Eco Warrior.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class Bunker extends GameObject {

    private final BunkerType type;
    private double spawnTimer = 0;
    private final double spawnInterval;
    private double pulseAnim = 0;

    public Bunker(double x, double y, BunkerType type, double spawnInterval) {
        super(x, y, 90, 90);
        this.type = type;
        this.spawnInterval = spawnInterval;
    }

    @Override
    public void update(double dt) {
        spawnTimer += dt;
        pulseAnim += dt * 3.0;
    }

    public boolean canSpawnUnit(int currentUnitCount, int maxUnits) {
        if (currentUnitCount < maxUnits && spawnTimer >= spawnInterval) {
            spawnTimer = 0;
            return true;
        }
        return false;
    }

    public BunkerType getType() {
        return type;
    }

    public double getSpawnPointX() {
        if (type == BunkerType.DESTROYER) {
            return x + width + 10;
        } else {
            return x - 30;
        }
    }

    public double getSpawnPointY() {
        return y + height / 2.0;
    }

    @Override
    public void render(Graphics2D g2d) {
        int bx = (int) x;
        int by = (int) y;
        int w = (int) width;
        int h = (int) height;

        double pulse = Math.sin(pulseAnim) * 3.0;

        if (type == BunkerType.DESTROYER) {
            // Visual Bunker Destroyer: Pac-man style gelap arang dengan moncong ke kanan
            // Sesuai ilustrasi halaman 7 diagram soal
            g2d.setColor(new Color(20, 20, 20, 100));
            g2d.fillOval(bx - 4, by + h - 14, w + 8, 20); // Bayangan

            // Badan bunker hitam pekat bergaya mulut terbuka ke arah kanan
            g2d.setColor(new Color(35, 35, 40));
            g2d.fillArc(bx, by, w, h, 40, 280);

            g2d.setColor(new Color(231, 76, 60));
            g2d.setStroke(new BasicStroke(3.0f));
            g2d.drawArc(bx, by, w, h, 40, 280);

            // Cerobong asap / lampu merah peringatan
            g2d.setColor(new Color(192, 57, 43));
            g2d.fillOval(bx + 18, by + 18, 14, 14);

            // Label bunker
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2d.drawString("BUNKER", bx + 12, by + h + 16);
            g2d.setColor(new Color(231, 76, 60));
            g2d.drawString("DESTROYER", bx + 4, by + h + 28);
        } else {
            // Visual Bunker Eco Warrior: Pac-man style hijau tosca / bio-dome dengan moncong ke kiri
            // Sesuai ilustrasi halaman 7 diagram soal
            g2d.setColor(new Color(20, 40, 20, 100));
            g2d.fillOval(bx - 4, by + h - 14, w + 8, 20); // Bayangan

            // Badan bunker hijau asri
            g2d.setColor(new Color(130, 204, 150));
            g2d.fillArc(bx, by, w, h, 220, 280);

            g2d.setColor(new Color(39, 174, 96));
            g2d.setStroke(new BasicStroke(3.0f));
            g2d.drawArc(bx, by, w, h, 220, 280);

            // Kubah hydro / tangki air energi
            g2d.setColor(new Color(52, 152, 219));
            int pSize = (int) (14 + pulse);
            g2d.fillOval(bx + w - 32, by + 18, pSize, pSize);

            // Label bunker
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2d.drawString("BUNKER", bx + 22, by + h + 16);
            g2d.setColor(new Color(46, 204, 113));
            g2d.drawString("ECO WARRIOR", bx + 4, by + h + 28);
        }
    }
}

