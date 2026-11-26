/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view.components;

import ecowarrior.audio.SoundManager;
import ecowarrior.config.GameConfig;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * Panel Kontrol Volume Audio (SoundControlPanel)
 * Menyediakan pengatur volume (slider) dan tombol mute sesuai spesifikasi fitur bonus (nilai 20).
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class SoundControlPanel extends JPanel {

    private final JSlider volumeSlider;
    private final JButton muteButton;
    private final JLabel volumeLabel;

    public SoundControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        setOpaque(false);

        SoundManager sm = SoundManager.getInstance();

        JLabel titleLabel = new JLabel("Volume Suara:");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        titleLabel.setForeground(GameConfig.COLOR_TEXT_LIGHT);
        add(titleLabel);

        volumeSlider = new JSlider(0, 100, (int) (sm.getVolume() * 100));
        volumeSlider.setPreferredSize(new Dimension(140, 24));
        volumeSlider.setOpaque(false);
        volumeSlider.setForeground(GameConfig.COLOR_ACCENT_GREEN);

        volumeLabel = new JLabel(volumeSlider.getValue() + "%");
        volumeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        volumeLabel.setForeground(GameConfig.COLOR_ACCENT_GREEN);
        volumeLabel.setPreferredSize(new Dimension(38, 20));

        volumeSlider.addChangeListener(e -> {
            int val = volumeSlider.getValue();
            volumeLabel.setText(val + "%");
            sm.setVolume(val / 100.0f);
        });
        add(volumeSlider);
        add(volumeLabel);

        muteButton = new ModernButton(sm.isMuted() ? "Unmute" : "Mute", new Color(70, 80, 75), new Color(90, 100, 95));
        muteButton.setPreferredSize(new Dimension(80, 28));
        muteButton.setFont(new Font("SansSerif", Font.PLAIN, 11));
        muteButton.addActionListener(e -> {
            sm.toggleMute();
            muteButton.setText(sm.isMuted() ? "Unmute" : "Mute");
        });
        add(muteButton);
    }
}

