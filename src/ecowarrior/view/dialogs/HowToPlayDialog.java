/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view.dialogs;

import ecowarrior.config.GameConfig;
import ecowarrior.view.components.ModernButton;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Dialog Panduan Bermain (How to Play) & Penjelasan Sistem Rank Buff.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class HowToPlayDialog extends JDialog {

    public HowToPlayDialog(Frame parent) {
        super(parent, "Panduan Permainan & Sistem Buff", true);
        setSize(620, 520);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(GameConfig.COLOR_PANEL_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("CARA BERMAIN & SISTEM GAME");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(GameConfig.COLOR_ACCENT_GREEN);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JTextArea infoText = new JTextArea();
        infoText.setEditable(false);
        infoText.setWrapStyleWord(true);
        infoText.setLineWrap(true);
        infoText.setFont(new Font("SansSerif", Font.PLAIN, 13));
        infoText.setBackground(new Color(20, 30, 24));
        infoText.setForeground(GameConfig.COLOR_TEXT_LIGHT);
        infoText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        infoText.setText(
            "1. LATAR BELAKANG & TUJUAN:\n" +
            "   - Bunker Destroyer (kiri bawah) memproduksi pion perusak yang membakar hutan.\n" +
            "   - Bunker Eco Warrior (kanan bawah) memproduksi pejuang pelindung bumi.\n" +
            "   - Tugas Anda: padamkan api dan lumpuhkan destroyer untuk menyelamatkan kelestarian hutan!\n\n" +
            "2. KONTROL MOUSE & KEYBOARD:\n" +
            "   - [KLIK MOUSE]: Klik pada Titik Api atau Destroyer di layar. Eco Warrior terdekat akan\n" +
            "     segera bergerak dan menembakkan meriam air/bom air bertekanan tinggi!\n" +
            "   - [TOMBOL SPACE]: Menghentikan permainan dan kembali ke menu awal secara aman.\n" +
            "     Seluruh perolehan poin dan riwayat akan otomatis tersimpan ke tabel tachievement.\n" +
            "   - [TOMBOL P]: Jeda (Pause / Resume) permainan.\n" +
            "   - [TOMBOL M]: Mengaktifkan / membisukan suara (Mute Audio).\n\n" +
            "3. SISTEM PEROLEHAN SKOR (ACHIEVEMENT):\n" +
            "   - Setiap Destroyer dilumpuhkan: +50 Skor Poin.\n" +
            "   - Setiap Titik Api dipadamkan: +25 Skor Poin.\n" +
            "   - Combo Multiplier aktif jika memadamkan api/destroyer secara beruntun!\n\n" +
            "4. MANFAAT PEROLEHAN ACHIEVEMENT PADA PERMAINAN BERIKUTNYA:\n" +
            "   - Skor achievement terakumulasi permanen di database untuk username Anda.\n" +
            "   - Rank 1: Ranger Recruit (0 - 499 pts) -> Kecepatan & semprotan air standar.\n" +
            "   - Rank 2: Forest Guardian (500 - 1199 pts) -> Kecepatan gerak pion +25%.\n" +
            "   - Rank 3: Hydro Specialist (1200 - 2499 pts) -> Daya padam air meningkat 1.5x.\n" +
            "   - Rank 4: Eco Commander (2500 - 4999 pts) -> Membuka Splash Damage Area.\n" +
            "   - Rank 5: Earth Legend (5000+ pts) -> Eco Warrior tercepat & regenerasi hutan aktif!"
        );

        JScrollPane scrollPane = new JScrollPane(infoText);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113, 80)));
        scrollPane.setPreferredSize(new Dimension(550, 340));

        ModernButton okBtn = new ModernButton("Mengerti", GameConfig.COLOR_ACCENT_GREEN, GameConfig.COLOR_ACCENT_HOVER);
        okBtn.setPreferredSize(new Dimension(110, 34));
        okBtn.setAlignmentX(CENTER_ALIGNMENT);
        okBtn.addActionListener(e -> dispose());

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));
        panel.add(okBtn);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
    }
}

