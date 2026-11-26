/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view.dialogs;

import ecowarrior.config.GameConfig;
import ecowarrior.model.RankTier;
import ecowarrior.view.components.ModernButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Dialog Hasil Akhir Permainan (Game Over Dialog)
 * Menampilkan rincian skor sesi, destroyer dilumpuhkan, api dipadamkan,
 * serta rank achievement yang berhasil dicapai dan tersimpan di database.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class GameOverDialog extends JDialog {

    private final Runnable onReturnToMenu;

    public GameOverDialog(Frame parent, String username, int score, int destroyer, 
                          int fire, int totalScore, boolean victory, Runnable onReturnToMenu) {
        super(parent, victory ? "Misi Berhasil!" : "Permainan Selesai", true);
        this.onReturnToMenu = onReturnToMenu;

        setSize(480, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JPanel content = new JPanel();
        content.setBackground(GameConfig.COLOR_PANEL_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel(victory ? "MISI SELESAI DENGAN GEMILANG!" : "HUTAN DALAM BAHAYA (GAME OVER)");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(victory ? GameConfig.COLOR_ACCENT_GREEN : GameConfig.COLOR_ACCENT_RED);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("Data statistik permainan telah tersimpan ke database tachievement");
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subLabel.setForeground(GameConfig.COLOR_TEXT_MUTED);
        subLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Panel Rincian Statistik
        JPanel statsPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        statsPanel.setBackground(new Color(20, 30, 24));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113, 80), 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        statsPanel.setMaximumSize(new Dimension(420, 160));

        addStatRow(statsPanel, "Pemain:", username, GameConfig.COLOR_TEXT_LIGHT);
        addStatRow(statsPanel, "Destroyer Dilumpuhkan:", String.valueOf(destroyer), new Color(231, 76, 60));
        addStatRow(statsPanel, "Titik Api Dipadamkan:", String.valueOf(fire), new Color(52, 152, 219));
        addStatRow(statsPanel, "Skor Sesi Ini:", "+" + score + " Poin", new Color(241, 196, 15));
        
        RankTier tier = RankTier.fromScore(totalScore);
        addStatRow(statsPanel, "Total Achievement Baru:", totalScore + " (" + tier.getDisplayName() + ")", tier.getBadgeColor());

        ModernButton menuBtn = new ModernButton("Kembali ke Menu Utama", GameConfig.COLOR_ACCENT_GREEN, GameConfig.COLOR_ACCENT_HOVER);
        menuBtn.setPreferredSize(new Dimension(200, 38));
        menuBtn.setAlignmentX(CENTER_ALIGNMENT);
        menuBtn.addActionListener(e -> {
            dispose();
            if (this.onReturnToMenu != null) {
                this.onReturnToMenu.run();
            }
        });

        content.add(titleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 6)));
        content.add(subLabel);
        content.add(Box.createRigidArea(new Dimension(0, 16)));
        content.add(statsPanel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(menuBtn);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(content, BorderLayout.CENTER);
    }

    private void addStatRow(JPanel panel, String label, String value, Color valColor) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(GameConfig.COLOR_TEXT_LIGHT);
        panel.add(l);

        JLabel v = new JLabel(value);
        v.setFont(new Font("SansSerif", Font.BOLD, 13));
        v.setForeground(valColor);
        panel.add(v);
    }
}

