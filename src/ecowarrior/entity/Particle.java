/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Entitas Partikel Efek Visual untuk animasi asap, percikan air, api, dan teks terapung.
 * Memberikan visual game yang 100x lebih memukau dan dinamis.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class Particle extends GameObject {

    public enum Type {
        WATER, SMOKE, EMBER, FLOATING_TEXT
    }

    private final Type type;
    private final Color color;
    private float alpha = 1.0f;
    private final float fadeSpeed;
    private final double size;
    private String text;

    public Particle(double x, double y, double vx, double vy, double size, Color color, float lifeTime, Type type) {
        super(x, y, size, size);
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.color = color;
        this.fadeSpeed = 1.0f / (lifeTime * 60.0f);
        this.type = type;
    }

    public static Particle createFloatingText(double x, double y, String text, Color color) {
        Particle p = new Particle(x, y, 0, -1.2, 14, color, 1.2f, Type.FLOATING_TEXT);
        p.text = text;
        return p;
    }

    @Override
    public void update(double dt) {
        x += vx;
        y += vy;
        alpha -= fadeSpeed;
        if (alpha <= 0.05f) {
            alpha = 0f;
            active = false;
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!active || alpha <= 0) return;

        var oldComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, Math.min(1f, alpha))));

        if (type == Type.FLOATING_TEXT && text != null) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2d.drawString(text, (int) x + 1, (int) y + 1);
            g2d.setColor(color);
            g2d.drawString(text, (int) x, (int) y);
        } else if (type == Type.SMOKE) {
            g2d.setColor(color);
            int s = (int) (size * (2.0f - alpha));
            g2d.fillOval((int) (x - s / 2), (int) (y - s / 2), s, s);
        } else {
            g2d.setColor(color);
            g2d.fillOval((int) x, (int) y, (int) size, (int) size);
        }

        g2d.setComposite(oldComposite);
    }
}

