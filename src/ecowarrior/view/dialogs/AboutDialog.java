/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view.dialogs;

import ecowarrior.config.GameConfig;
import ecowarrior.view.components.ModernButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Dialog Informasi Proyek & Pembacaan Janji Integritas Akademik.
 * Menampilkan teks janji resmi yang dibacakan saat presentasi video.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class AboutDialog extends JDialog {

    public AboutDialog(Frame parent) {
        super(parent, "Tentang Program & Janji Integritas", true);
        setSize(560, 480);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(GameConfig.COLOR_PANEL_BG);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("ECO WARRIOR DPBO 2026");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(GameConfig.COLOR_ACCENT_GREEN);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel infoLabel = new JLabel("<html><center>Pengembang: <b>" + GameConfig.STUDENT_NAME 
                + "</b> (NIM: " + GameConfig.STUDENT_NIM + ")<br>Kelas: " 
                + GameConfig.STUDENT_CLASS + " | Dosen: Rosa Ariani Sukamto<br>Program Studi Ilmu Komputer FPMIPA UPI</center></html>");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        infoLabel.setForeground(GameConfig.COLOR_TEXT_LIGHT);
        infoLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel janjiHeader = new JLabel("--- Janji Pengerjaan Mandiri ---");
        janjiHeader.setFont(new Font("SansSerif", Font.BOLD, 14));
        janjiHeader.setForeground(new Color(241, 196, 15));
        janjiHeader.setAlignmentX(CENTER_ALIGNMENT);

        JTextArea janjiText = new JTextArea();
        janjiText.setText(
            "\"Saya Najib Nurohman sebagai manusia yang berTuhan, " +
            "saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah " +
            "Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya " +
            "maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.\""
        );
        janjiText.setWrapStyleWord(true);
        janjiText.setLineWrap(true);
        janjiText.setEditable(false);
        janjiText.setFocusable(false);
        janjiText.setFont(new Font("SansSerif", Font.ITALIC, 13));
        janjiText.setBackground(new Color(18, 28, 22));
        janjiText.setForeground(Color.WHITE);
        janjiText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113, 120), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        janjiText.setMaximumSize(new Dimension(500, 110));

        ModernButton closeBtn = new ModernButton("Tutup", GameConfig.COLOR_ACCENT_GREEN, GameConfig.COLOR_ACCENT_HOVER);
        closeBtn.setPreferredSize(new Dimension(100, 34));
        closeBtn.setAlignmentX(CENTER_ALIGNMENT);
        closeBtn.addActionListener(e -> dispose());

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(infoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        contentPanel.add(janjiHeader);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(janjiText);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(closeBtn);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }
}

