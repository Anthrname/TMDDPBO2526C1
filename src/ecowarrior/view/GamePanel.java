/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view;

import ecowarrior.config.GameConfig;
import ecowarrior.entity.Bunker;
import ecowarrior.entity.Destroyer;
import ecowarrior.entity.EcoWarrior;
import ecowarrior.entity.Fire;
import ecowarrior.entity.Particle;
import ecowarrior.entity.WaterProjectile;
import ecowarrior.model.GameState;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.JPanel;

/**
 * Kanvas Visual Utama Permainan (GamePanel)
 * Menggunakan Double Buffering dan rendering Graphics2D anti-aliased berkualitas tinggi.
 * Menggambar seluruh dunia ekosistem hutan, bunker, pion, efek tembakan air, dan HUD dinamis.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class GamePanel extends JPanel {

    private GameState gameState;
    private int mouseX = -1;
    private int mouseY = -1;
    private BiConsumer<Integer, Integer> mouseClickHandler;

    public GamePanel() {
        setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT));
        setBackground(new Color(24, 44, 30));
        setDoubleBuffered(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (mouseClickHandler != null) {
                    mouseClickHandler.accept(e.getX(), e.getY());
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public void setMouseClickHandler(BiConsumer<Integer, Integer> handler) {
        this.mouseClickHandler = handler;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Aktifkan Anti-Aliasing untuk grafis halus dan tajam
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();

        // 1. Gambar Latar Belakang Lanskap Hutan Alami
        drawForestBackground(g2d, width, height);

        if (gameState != null) {
            // 2. Gambar Bunker
            Bunker bDestroyer = gameState.getBunkerDestroyer();
            if (bDestroyer != null) bDestroyer.render(g2d);

            Bunker bEco = gameState.getBunkerEcoWarrior();
            if (bEco != null) bEco.render(g2d);

            // 3. Gambar Titik Api
            List<Fire> fires = gameState.getFires();
            synchronized (fires) {
                for (Fire f : fires) {
                    if (f.isActive()) f.render(g2d);
                }
            }

            // 4. Gambar Destroyer
            List<Destroyer> destroyers = gameState.getDestroyers();
            synchronized (destroyers) {
                for (Destroyer d : destroyers) {
                    if (d.isActive()) d.render(g2d);
                }
            }

            // 5. Gambar Eco Warrior
            List<EcoWarrior> warriors = gameState.getEcoWarriors();
            synchronized (warriors) {
                for (EcoWarrior w : warriors) {
                    if (w.isActive()) w.render(g2d);
                }
            }

            // 6. Gambar Proyektil Tembakan Air
            List<WaterProjectile> projectiles = gameState.getWaterProjectiles();
            synchronized (projectiles) {
                for (WaterProjectile p : projectiles) {
                    if (p.isActive()) p.render(g2d);
                }
            }

            // 7. Gambar Partikel Efek
            List<Particle> particles = gameState.getParticles();
            synchronized (particles) {
                for (Particle pt : particles) {
                    if (pt.isActive()) pt.render(g2d);
                }
            }

            // 8. Gambar Retikel Kursor Bidikan Mouse
            drawTargetReticle(g2d);

            // 9. Gambar HUD (Heads-Up Display) di bagian atas layar
            drawHUD(g2d, width);

            // 10. Overlay Jeda jika Game di-Pause
            if (gameState.isPaused()) {
                drawPauseOverlay(g2d, width, height);
            }
        }

        g2d.dispose();
    }

    private void drawForestBackground(Graphics2D g2d, int w, int h) {
        // Gradien langit sore/kehutanan
        GradientPaint skyGradient = new GradientPaint(0, 0, new Color(40, 80, 60), 0, h * 0.45f, new Color(28, 55, 38));
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, w, h);

        // Lapisan pepohonan kejauhan
        g2d.setColor(new Color(22, 45, 32));
        for (int i = 0; i < w; i += 70) {
            int treeHeight = 110 + (int) (Math.sin(i * 0.05) * 25);
            g2d.fillOval(i - 20, (int) (h * 0.28) - treeHeight, 90, treeHeight + 60);
        }

        // Lapisan tanah hutan (Forest Ground)
        GradientPaint groundGradient = new GradientPaint(0, h * 0.35f, new Color(34, 70, 45), 0, h, new Color(20, 44, 28));
        g2d.setPaint(groundGradient);
        g2d.fillRect(0, (int) (h * 0.35), w, (int) (h * 0.65));

        // Tekstur rerumputan dan alur sungai kecil
        g2d.setColor(new Color(41, 88, 56, 120));
        for (int x = 40; x < w; x += 120) {
            for (int y = (int) (h * 0.42); y < h - 80; y += 90) {
                g2d.fillOval(x + (y % 40), y, 50, 16);
            }
        }

        // Alur sungai air segar di tengah bawah
        g2d.setColor(new Color(41, 128, 185, 90));
        g2d.setStroke(new BasicStroke(16.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(w / 2 - 120, h, w / 2 + 140, (int) (h * 0.55));
    }

    private void drawTargetReticle(Graphics2D g2d) {
        if (mouseX < 0 || mouseY < 0) return;

        g2d.setColor(new Color(52, 152, 219, 180));
        g2d.setStroke(new BasicStroke(1.5f));
        int r = 16;
        g2d.drawOval(mouseX - r, mouseY - r, r * 2, r * 2);
        g2d.drawLine(mouseX - r - 6, mouseY, mouseX + r + 6, mouseY);
        g2d.drawLine(mouseX, mouseY - r - 6, mouseX, mouseY + r + 6);
    }

    private void drawHUD(Graphics2D g2d, int w) {
        // Kotak panel HUD atas bergaya Glassmorphism
        int hudH = 75;
        g2d.setColor(new Color(15, 25, 20, 220));
        g2d.fillRect(15, 12, w - 30, hudH);
        g2d.setColor(new Color(46, 204, 113, 140));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(15, 12, w - 30, hudH, 12, 12);

        // Kiri: Info Pemain & Rank Buff
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 15));
        g2d.drawString("Pejuang: " + gameState.getUsername(), 30, 36);

        var rank = gameState.getRankTier();
        g2d.setColor(rank.getBadgeColor());
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.drawString("Rank: " + rank.getDisplayName() + " (Speed x" + rank.getSpeedMultiplier() + ")", 30, 56);

        // Tengah: Statistik Soal (Achievement, Destroyer, Fire)
        // Sesuai format teks pada diagram soal halaman 7:
        // achievement: 101 | destroyer: 10 | fire: 20
        int cx = w / 2 - 180;
        g2d.setColor(new Color(241, 196, 15));
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2d.drawString("achievement: " + gameState.getTotalAchievementScore(), cx, 36);

        g2d.setColor(new Color(231, 76, 60));
        g2d.drawString("destroyer: " + gameState.getSessionDestroyer(), cx, 58);

        g2d.setColor(new Color(52, 152, 219));
        g2d.drawString("fire: " + gameState.getSessionFire(), cx + 160, 58);

        // Combo Multiplier jika ada
        if (gameState.getCombo() > 1) {
            g2d.setColor(new Color(255, 110, 0));
            g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2d.drawString("COMBO x" + gameState.getCombo() + "!", cx + 160, 36);
        }

        // Kanan: Bar Kesehatan Hutan (Forest Health Bar)
        int rx = w - 330;
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2d.drawString("Kelestarian Hutan: " + (int) gameState.getForestHealth() + "%", rx, 34);

        int barW = 290;
        int barH = 16;
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRoundRect(rx, 42, barW, barH, 8, 8);

        double healthRatio = gameState.getForestHealth() / 100.0;
        Color barColor = (healthRatio > 0.5) ? GameConfig.COLOR_ACCENT_GREEN :
                         (healthRatio > 0.25) ? Color.ORANGE : GameConfig.COLOR_ACCENT_RED;
        g2d.setColor(barColor);
        g2d.fillRoundRect(rx, 42, (int) (barW * healthRatio), barH, 8, 8);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawRoundRect(rx, 42, barW, barH, 8, 8);

        // Panduan Cepat di bawah HUD
        g2d.setColor(new Color(220, 240, 230, 200));
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.drawString("[SPACE] Simpan & Kembali ke Menu | [KLIK MOUSE] Arahkan Eco Warrior terdekat | [P] Pause | [M] Mute", 30, 106);
    }

    private void drawPauseOverlay(Graphics2D g2d, int w, int h) {
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(0, 0, w, h);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 36));
        String text = "PERMAINAN DIJEDA (PAUSED)";
        int tw = g2d.getFontMetrics().stringWidth(text);
        g2d.drawString(text, (w - tw) / 2, h / 2 - 20);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
        String sub = "Tekan tombol [P] untuk melanjutkan atau [SPACE] untuk kembali ke Menu";
        int sw = g2d.getFontMetrics().stringWidth(sub);
        g2d.drawString(sub, (w - sw) / 2, h / 2 + 25);
    }
}

