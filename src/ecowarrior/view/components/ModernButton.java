/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 * Komponen tombol modern dengan sudut melengkung, efek hover animasi halus,
 * dan bayangan estetik untuk antarmuka pengguna GUI yang elegan.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class ModernButton extends JButton {

    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public ModernButton(String text, Color baseColor, Color hoverColor) {
        super(text);
        this.normalColor = baseColor;
        this.hoverColor = hoverColor;
        this.pressedColor = baseColor.darker();

        setFont(new Font("SansSerif", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 14;
        Color bg = normalColor;
        if (isPressed) {
            bg = pressedColor;
        } else if (isHovered) {
            bg = hoverColor;
        }

        // Efek bayangan halus
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 4, arc, arc);

        // Badan tombol
        g2d.setColor(bg);
        g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, arc, arc);

        g2d.dispose();
        super.paintComponent(g);
    }
}

