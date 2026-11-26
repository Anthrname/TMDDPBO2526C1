package ecowarrior.assets;

import ecowarrior.view.IsometricRenderer;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Asset atlas: mengelola gambar (tiles/sprites) dan menyajikannya dengan
 * proyeksi isometric (2.5D). Mendukung sprite tunggal maupun animasi pergerakan.
 *
 * <p>Gunakan {@code Atlas.INSTANCE} sebagai titik akses tunggal dari seluruh
 * layer VIEW (GamePanel, entity). Semua gambar di-load dari folder
 * {@code resources/images} oleh {@code TextureManager}.</p>
 */
public class Atlas {

    /** Instance tunggal yang dipakai seluruh lapisan view. */
    public static final Atlas INSTANCE = new Atlas();

    /** Definisi gambar (single atau animasi). */
    private interface Slot {}

    private static final class Single implements Slot {
        final BufferedImage img;
        Single(BufferedImage img) { this.img = img; }
    }

    private static final class Anim implements Slot {
        final BufferedImage[] frames;
        Anim(BufferedImage[] frames) { this.frames = frames; }
    }

    /** Peta milik asset: kunci -> definisi gambar. */
    private final Map<String, Slot> slots = new HashMap<>();

    private Atlas() {}

    /** Daftarkan sebuah gambar tunggal (tidak ada animasi). */
    public void addNamed(String id, BufferedImage image) {
        slots.put(id, new Single(image));
    }

    /** Daftarkan serangkaian frame gambar sebagai animasi. */
    public void addAnimated(String id, BufferedImage[] frames) {
        slots.put(id, new Anim(frames));
    }

    /**
     * Menggambar sprite yang terdaftar ke layar menurut proyeksi isometric.
     *
     * @param g2d konteks grafis
     * @param id    kunci asset di dalam atlas
     * @param x,y,z koordinat dunia (dalam world-isometrik)
     * @param w,h   tinggi layar yang diinginkan (pixel) untuk ukuran sprite
     * @param now   milidetik game-time, menggerakkan animasi
     */
    public void draw(Graphics2D g2d, String id, double x, double y, double z,
                     int w, int h, long now) {
        Slot slot = slots.get(id);
        if (slot instanceof Anim anim) {
            int frame = (int)((now * 0.00028) % anim.frames.length);
            drawImage(g2d, anim.frames[frame], x, y, z, w, h);
        } else if (slot instanceof Single single) {
            drawImage(g2d, single.img, x, y, z, w, h);
        }
    }

    /**
     * Proyeksi koordinat dunia + render gambar ke kanvas {@code g2d} (isometrik).
     */
    private void drawImage(Graphics2D g2d, BufferedImage img,
                           double x, double y, double z,
                           int width, int height) {
        if (img == null) return;
        java.awt.geom.Point2D.Double p = IsometricRenderer.isoToScreen(x, y, z);

        int iw = img.getWidth();
        int ih = img.getHeight();
        double scale = (double) height / (double) ih;
        int dw = (int) (iw * scale);
        int dh = height;
        int px = (int) (p.x - dw / 2.0);
        int py = (int) (p.y - dh / 2.0);

        g2d.drawImage(img, px, py, dw, dh, null);
    }

    /**
     * Render sprite di layar (koordinat screen-space ortogonal, tanpa proyeksi isometric).
     * Digunakan oleh entity yang ditempatkan lewat koordinat layar (x,y) seperti Bunker.
     */
    public void drawScreen(Graphics2D g2d, String id, double sx, double sy, double z,
                           int w, int h, long now) {
        Slot slot = slots.get(id);
        if (slot instanceof Anim anim) {
            int frame = (int)((now * 0.00028) % anim.frames.length);
            drawImageScreen(g2d, anim.frames[frame], sx, sy, w, h);
        } else if (slot instanceof Single single) {
            drawImageScreen(g2d, single.img, sx, sy, w, h);
        }
    }

    /** Helper: gambarkan gambar langsung di koordinat layar (sx,sy) tanpa proyeksi. */
    private void drawImageScreen(Graphics2D g2d, BufferedImage img,
                                  double sx, double sy, int w, int h) {
        if (img == null) return;
        int iw = img.getWidth();
        int ih = img.getHeight();
        double scale = (double) h / (double) ih;   // ringkankan sesuai tinggi layar
        int dw = (int) (iw * scale);
        int dh = h;
        int px = (int) (sx - dw / 2.0);
        int py = (int) (sy - dh / 2.0);
        g2d.drawImage(img, px, py, dw, dh, null);
    }
}
