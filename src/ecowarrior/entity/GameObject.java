/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.entity;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Kelas Abstrak GameObject sebagai cetak biru (blueprint) seluruh objek di dalam game.
 * Menerapkan prinsip Abstraction dan Polymorphism dalam OOP.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public abstract class GameObject {
    protected double x;
    protected double y;
    protected double vx;
    protected double vy;
    protected double width;
    protected double height;
    protected boolean active = true;

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.vx = 0;
        this.vy = 0;
    }

    /**
     * Memperbarui logika posisi dan state objek tiap frame (delta time).
     * 
     * @param dt delta time dalam satuan detik
     */
    public abstract void update(double dt);

    /**
     * Menggambar objek ke kanvas grafis 2D.
     * 
     * @param g2d objek Graphics2D dengan rendering hints
     */
    public abstract void render(Graphics2D g2d);

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getVx() { return vx; }
    public void setVx(double vx) { this.vx = vx; }

    public double getVy() { return vy; }
    public void setVy(double vy) { this.vy = vy; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public double getCenterX() {
        return x + width / 2.0;
    }

    public double getCenterY() {
        return y + height / 2.0;
    }

    public double distanceTo(GameObject other) {
        if (other == null) return Double.MAX_VALUE;
        double dx = getCenterX() - other.getCenterX();
        double dy = getCenterY() - other.getCenterY();
        return Math.hypot(dx, dy);
    }

    public double distanceTo(double targetX, double targetY) {
        double dx = getCenterX() - targetX;
        double dy = getCenterY() - targetY;
        return Math.hypot(dx, dy);
    }

    public boolean intersects(GameObject other) {
        if (other == null || !other.active) return false;
        return getBounds().intersects(other.getBounds());
    }

    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }
}

