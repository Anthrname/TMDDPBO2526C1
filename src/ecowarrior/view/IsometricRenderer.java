/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view;

import ecowarrior.config.GameConfig;
import ecowarrior.entity.Bunker;
import ecowarrior.entity.BunkerType;
import ecowarrior.entity.Destroyer;
import ecowarrior.entity.EcoWarrior;
import ecowarrior.entity.Fire;
import ecowarrior.entity.GameObject;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Mesin Rendering Isometrik 2.5D / Pseudo-3D (IsometricRenderer).
 * 
 * Mengubah perspektif visual game ECO WARRIOR menjadi tampilan 3D isometrik bertaraf modern:
 * 1. Proyeksi matematika isometrik 2:1 (World (x,y,z) -> Screen (sx, sy)).
 * 2. Lanskap medan 3D berlapis (elevasi tebing, sungai beralur, ubin berlian bertingkat).
 * 3. Model struktur 3D voxel: Bunker Destroyer (benteng besi berasap) & Bunker Eco Warrior (bio-dome kristal).
 * 4. Model karakter 3D: Eco Warrior ber-jetpack air & Destroyer bertabung gas dan obor.
 * 5. Efek pencahayaan tanah dinamis (Radial Light Cast dari api) dan bayangan objek 3D.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class IsometricRenderer {

    // Rasio standar isometrik 2:1
    public static final double TILE_W = 64.0;
    public static final double TILE_H = 32.0;
    public static final double TILE_HALF_W = TILE_W / 2.0;
    public static final double TILE_HALF_H = TILE_H / 2.0;

    // Titik pusat proyeksi di layar
    public static final double ORIGIN_X = GameConfig.WINDOW_WIDTH / 2.0;
    public static final double ORIGIN_Y = 130.0;

    // Data pohon statis dekorasi hutan 3D
    public static class IsometricTree {
        public double wx, wy, wz;
        public int type; // 0 = Pine, 1 = Oak
        public double scale;

        public IsometricTree(double wx, double wy, double wz, int type, double scale) {
            this.wx = wx;
            this.wy = wy;
            this.wz = wz;
            this.type = type;
            this.scale = scale;
        }
    }

    private static final List<IsometricTree> DECORATIVE_TREES = new ArrayList<>();

    static {
        // Inisialisasi sebaran pepohonan hutan 3D di koordinat dunia isometrik
        double[][] treeCoords = {
            {2.0, 3.0, 0}, {3.5, 1.5, 0}, {5.0, 4.0, 0}, {1.5, 7.0, 0},
            {14.0, 2.5, 0}, {16.0, 4.0, 0}, {13.0, 6.0, 0}, {15.5, 8.0, 0},
            {4.0, 14.0, 0}, {2.5, 12.0, 0}, {6.0, 15.0, 0},
            {12.0, 13.0, 0}, {14.5, 15.0, 0}, {16.0, 12.0, 0},
            {8.0, 2.0, 0}, {10.5, 3.0, 0}, {7.5, 16.0, 0}, {10.0, 15.5, 0}
        };
        for (int i = 0; i < treeCoords.length; i++) {
            DECORATIVE_TREES.add(new IsometricTree(
                treeCoords[i][0], treeCoords[i][1], treeCoords[i][2],
                i % 2, 0.85 + (i % 3) * 0.15
            ));
        }
    }

    /**
     * Konversi koordinat dunia isometrik (wx, wy, wz) ke koordinat layar 2D (sx, sy).
     */
    public static Point2D.Double isoToScreen(double wx, double wy, double wz) {
        double sx = ORIGIN_X + (wx - wy) * TILE_HALF_W;
        double sy = ORIGIN_Y + (wx + wy) * TILE_HALF_H - wz;
        return new Point2D.Double(sx, sy);
    }

    /**
     * Konversi koordinat layar 2D (sx, sy) ke koordinat bidang tanah isometrik (wx, wy).
     */
    public static Point2D.Double screenToIso(double sx, double sy) {
        double dx = sx - ORIGIN_X;
        double dy = sy - ORIGIN_Y;
        double wx = (dx / TILE_HALF_W + dy / TILE_HALF_H) / 2.0;
        double wy = (dy / TILE_HALF_H - dx / TILE_HALF_W) / 2.0;
        return new Point2D.Double(wx, wy);
    }

    /**
     * Menggambar seluruh latar belakang alam hutan 3D:
     * - Langit atmosferik gradien fajar/senja hutan
     * - Grid ubin isometrik berpola berlian (Diamond isometric tiles) dengan elevasi
     * - Tebing berbatu dengan bayangan kedalaman 3D
     * - Aliran sungai membelah lembah dengan pantulan air jernih
     */
    public static void renderIsometricTerrain(Graphics2D g2d, int screenW, int screenH) {
        // 1. Langit Atmosferik Luar Angkasa/Hutan (Sky Gradient)
        GradientPaint skyGradient = new GradientPaint(
            0, 0, new Color(15, 32, 25),
            0, screenH, new Color(24, 48, 36)
        );
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, screenW, screenH);

        // 2. Kabut Pegunungan Kejauhan (Distant Horizon Silhouettes)
        g2d.setColor(new Color(20, 42, 32, 180));
        Polygon mountains = new Polygon();
        mountains.addPoint(0, 260);
        mountains.addPoint(120, 180);
        mountains.addPoint(280, 240);
        mountains.addPoint(460, 150);
        mountains.addPoint(620, 220);
        mountains.addPoint(800, 160);
        mountains.addPoint(960, 210);
        mountains.addPoint(screenW, 170);
        mountains.addPoint(screenW, 400);
        mountains.addPoint(0, 400);
        g2d.fill(mountains);

        // 3. Gambar Ubin Tanah Isometrik (18 x 18 Tiles)
        int gridSize = 18;
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                drawIsometricTile(g2d, x, y);
            }
        }

        // 4. Tebing Bawah Isometrik (Cliff Extrusion Faces untuk efek 3D Solid Slab)
        drawIsometricCliffFaces(g2d, gridSize);
    }

    private static void drawIsometricTile(Graphics2D g2d, int tx, int ty) {
        Point2D.Double pTop = isoToScreen(tx, ty, 0);
        Point2D.Double pRight = isoToScreen(tx + 1, ty, 0);
        Point2D.Double pBottom = isoToScreen(tx + 1, ty + 1, 0);
        Point2D.Double pLeft = isoToScreen(tx, ty + 1, 0);

        Polygon tilePoly = new Polygon();
        tilePoly.addPoint((int) pTop.x, (int) pTop.y);
        tilePoly.addPoint((int) pRight.x, (int) pRight.y);
        tilePoly.addPoint((int) pBottom.x, (int) pBottom.y);
        tilePoly.addPoint((int) pLeft.x, (int) pLeft.y);

        // Jalur Sungai Membelah Lembah (Diagonal tx + ty di area tengah)
        boolean isRiver = Math.abs((tx - ty)) <= 1 && tx >= 4 && tx <= 14;
        boolean isPath = (tx == 9 || ty == 9) && !isRiver;

        if (isRiver) {
            // Air sungai biru toska dengan animasi pantulan
            g2d.setColor(new Color(41, 128, 185));
            g2d.fill(tilePoly);
            g2d.setColor(new Color(84, 153, 199, 140));
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine((int) pLeft.x + 6, (int) pLeft.y, (int) pRight.x - 6, (int) pRight.y);
        } else if (isPath) {
            // Jalan setapak tanah berbatu
            g2d.setColor(new Color(110, 89, 74));
            g2d.fill(tilePoly);
        } else {
            // Rumput hijau asri bervariasi pola kotak catur halus (Zebra grass checker)
            if ((tx + ty) % 2 == 0) {
                g2d.setColor(new Color(46, 125, 75));
            } else {
                g2d.setColor(new Color(40, 115, 68));
            }
            g2d.fill(tilePoly);
        }

        // Garis batas ubin isometrik halus
        g2d.setColor(new Color(30, 80, 50, 60));
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.draw(tilePoly);
    }

    private static void drawIsometricCliffFaces(Graphics2D g2d, int size) {
        int cliffHeight = 35;

        // Sisi Tebing Kanan Bawah
        for (int x = 0; x < size; x++) {
            Point2D.Double p1 = isoToScreen(x, size, 0);
            Point2D.Double p2 = isoToScreen(x + 1, size, 0);

            Polygon side = new Polygon();
            side.addPoint((int) p1.x, (int) p1.y);
            side.addPoint((int) p2.x, (int) p2.y);
            side.addPoint((int) p2.x, (int) p2.y + cliffHeight);
            side.addPoint((int) p1.x, (int) p1.y + cliffHeight);

            // Shading tebing kanan (lebih gelap)
            g2d.setColor(new Color(50, 40, 32));
            g2d.fill(side);
            g2d.setColor(new Color(35, 28, 22));
            g2d.draw(side);
        }

        // Sisi Tebing Kiri Bawah
        for (int y = 0; y < size; y++) {
            Point2D.Double p1 = isoToScreen(size, y, 0);
            Point2D.Double p2 = isoToScreen(size, y + 1, 0);

            Polygon side = new Polygon();
            side.addPoint((int) p1.x, (int) p1.y);
            side.addPoint((int) p2.x, (int) p2.y);
            side.addPoint((int) p2.x, (int) p2.y + cliffHeight);
            side.addPoint((int) p1.x, (int) p1.y + cliffHeight);

            // Shading tebing kiri (sedikit lebih terang terkena arah cahaya)
            g2d.setColor(new Color(75, 58, 46));
            g2d.fill(side);
            g2d.setColor(new Color(45, 35, 28));
            g2d.draw(side);
        }
    }

    /**
     * Menggambar pohon dekorasi 3D bertingkat (Low-Poly 3D Trees)
     */
    public static void renderTree(Graphics2D g2d, IsometricTree tree) {
        Point2D.Double pos = isoToScreen(tree.wx, tree.wy, tree.wz);
        int bx = (int) pos.x;
        int by = (int) pos.y;
        double s = tree.scale;

        // Bayangan pohon di tanah
        g2d.setColor(new Color(15, 30, 20, 110));
        g2d.fillOval(bx - (int) (18 * s), by - (int) (6 * s), (int) (36 * s), (int) (14 * s));

        // Batang Pohon Silindris 3D
        g2d.setColor(new Color(90, 60, 40));
        g2d.fillRect(bx - (int) (4 * s), by - (int) (22 * s), (int) (8 * s), (int) (22 * s));
        g2d.setColor(new Color(60, 40, 25));
        g2d.fillRect(bx, by - (int) (22 * s), (int) (4 * s), (int) (22 * s)); // Shading batang

        if (tree.type == 0) {
            // Pohon Pinus 3D Kerucut Bertingkat (Conical Pine)
            drawPineCone(g2d, bx, by - (int) (16 * s), (int) (28 * s), (int) (24 * s), new Color(34, 112, 63), new Color(25, 85, 48));
            drawPineCone(g2d, bx, by - (int) (32 * s), (int) (22 * s), (int) (20 * s), new Color(39, 130, 74), new Color(29, 98, 56));
            drawPineCone(g2d, bx, by - (int) (46 * s), (int) (16 * s), (int) (16 * s), new Color(46, 150, 85), new Color(34, 112, 63));
        } else {
            // Pohon Rindang Bulat 3D (Broadleaf Oak)
            drawOakFoliage(g2d, bx, by - (int) (36 * s), (int) (22 * s));
        }
    }

    private static void drawPineCone(Graphics2D g2d, int cx, int baseCy, int w, int h, Color lightC, Color darkC) {
        Polygon leftHalf = new Polygon();
        leftHalf.addPoint(cx, baseCy - h);
        leftHalf.addPoint(cx - w / 2, baseCy);
        leftHalf.addPoint(cx, baseCy);
        g2d.setColor(lightC);
        g2d.fill(leftHalf);

        Polygon rightHalf = new Polygon();
        rightHalf.addPoint(cx, baseCy - h);
        rightHalf.addPoint(cx + w / 2, baseCy);
        rightHalf.addPoint(cx, baseCy);
        g2d.setColor(darkC);
        g2d.fill(rightHalf);
    }

    private static void drawOakFoliage(Graphics2D g2d, int cx, int cy, int r) {
        g2d.setColor(new Color(30, 100, 55));
        g2d.fillOval(cx - r, cy - r, r * 2, r * 2);
        // Sorotan cahaya di bagian kiri atas tajuk daun
        g2d.setColor(new Color(46, 139, 87));
        g2d.fillOval(cx - r + 3, cy - r + 2, (int) (r * 1.5), (int) (r * 1.5));
        g2d.setColor(new Color(76, 175, 80));
        g2d.fillOval(cx - r + 6, cy - r + 4, r, r);
    }

    /**
     * Menggambar model 3D Voxel Bunker Destroyer:
     * Benteng baja industrial heksagonal dengan cerobong asap tebal dan lampu hazard merah.
     */
    public static void render3DDestroyerBunker(Graphics2D g2d, double screenX, double screenY) {
        int bx = (int) screenX + 35;
        int by = (int) screenY + 40;

        // Bayangan besar benteng di tanah
        g2d.setColor(new Color(10, 15, 12, 140));
        g2d.fillOval(bx - 55, by - 15, 110, 45);

        // Fondasi Beton Bawah (Prisma Isometrik)
        drawIsometricPrism(g2d, bx, by, 70, 45, 25, new Color(45, 52, 54), new Color(30, 39, 46), new Color(60, 68, 70));

        // Blok Baja Utama Tingkat 2
        drawIsometricPrism(g2d, bx, by - 25, 50, 32, 30, new Color(53, 59, 72), new Color(38, 42, 53), new Color(70, 78, 92));

        // Garis Peringatan Kuning-Hitam Bahaya (Hazard Stripes)
        g2d.setColor(new Color(241, 196, 15));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawLine(bx - 20, by - 12, bx - 10, by - 20);
        g2d.drawLine(bx - 5, by - 12, bx + 5, by - 20);
        g2d.drawLine(bx + 10, by - 12, bx + 20, by - 20);

        // Pintu Gerbang Hidrolik Pembakar (Tungku Menyala Merah Membara)
        g2d.setColor(new Color(231, 76, 60));
        g2d.fillOval(bx + 8, by - 10, 22, 14);
        g2d.setColor(new Color(255, 140, 0));
        g2d.fillOval(bx + 11, by - 8, 16, 10);

        // Cerobong Asap Baja Kembar (Twin Smokestacks)
        drawIsometricPrism(g2d, bx - 14, by - 55, 12, 12, 28, new Color(40, 40, 45), new Color(25, 25, 30), new Color(60, 60, 65));
        drawIsometricPrism(g2d, bx + 12, by - 55, 12, 12, 34, new Color(40, 40, 45), new Color(25, 25, 30), new Color(60, 60, 65));

        // Label Teks 3D
        g2d.setFont(new Font("SansSerif", Font.BOLD, 11));
        g2d.setColor(Color.WHITE);
        g2d.drawString("BUNKER DESTROYER", bx - 55, by + 40);
        g2d.setColor(new Color(231, 76, 60));
        g2d.drawString("[ARSONIST FACTORY]", bx - 50, by + 54);
    }

    /**
     * Menggambar model 3D Voxel Bunker Eco Warrior:
     * Kubah kaca geodesik (Bio-Dome) dengan kristal hydro-generator biru dan pipa air bertekanan.
     */
    public static void render3DEcoWarriorBunker(Graphics2D g2d, double screenX, double screenY) {
        int bx = (int) screenX + 45;
        int by = (int) screenY + 40;

        // Bayangan bio-dome di tanah
        g2d.setColor(new Color(10, 25, 18, 140));
        g2d.fillOval(bx - 60, by - 15, 120, 50);

        // Cincin Fondasi Hijau Ekologis
        drawIsometricPrism(g2d, bx, by, 76, 50, 20, new Color(39, 174, 96), new Color(30, 132, 73), new Color(46, 204, 113));

        // Tangki Air Silinder Kiri
        drawIsometricPrism(g2d, bx - 28, by - 20, 18, 18, 28, new Color(41, 128, 185), new Color(31, 97, 141), new Color(52, 152, 219));

        // Kubah Kaca Geodesik Transparan (Bio-Dome Spherical Prism)
        g2d.setColor(new Color(52, 152, 219, 170));
        g2d.fillOval(bx - 26, by - 58, 52, 50);

        // Refleksi Kilau Cahaya Kaca
        g2d.setColor(new Color(255, 255, 255, 190));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawArc(bx - 20, by - 52, 40, 36, 60, 70);

        // Inti Reaktor Kristal Air (Pulsing Hydro-Core)
        g2d.setColor(new Color(0, 220, 255));
        g2d.fillOval(bx - 10, by - 42, 20, 20);

        // Panel Surya Daun (Solar Eco-Panels)
        g2d.setColor(new Color(26, 188, 156));
        g2d.fillRoundRect(bx + 16, by - 36, 18, 12, 4, 4);

        // Label Teks 3D
        g2d.setFont(new Font("SansSerif", Font.BOLD, 11));
        g2d.setColor(Color.WHITE);
        g2d.drawString("BUNKER ECO WARRIOR", bx - 60, by + 40);
        g2d.setColor(new Color(46, 204, 113));
        g2d.drawString("[HYDRO COMMAND HUB]", bx - 55, by + 54);
    }

    /**
     * Utilitas Menggambar Balok Prisma Isometrik 3D Berbayang (Isometric 3D Prism).
     */
    private static void drawIsometricPrism(Graphics2D g2d, int cx, int cy, int w, int d, int h,
                                          Color topC, Color leftC, Color rightC) {
        int hw = w / 2;
        int hd = d / 2;

        // Wajah Atas (Top Face)
        Polygon topFace = new Polygon();
        topFace.addPoint(cx, cy - h - hd);
        topFace.addPoint(cx + hw, cy - h);
        topFace.addPoint(cx, cy - h + hd);
        topFace.addPoint(cx - hw, cy - h);
        g2d.setColor(topC);
        g2d.fill(topFace);
        g2d.setColor(topC.darker());
        g2d.draw(topFace);

        // Wajah Kiri (Left Face)
        Polygon leftFace = new Polygon();
        leftFace.addPoint(cx - hw, cy - h);
        leftFace.addPoint(cx, cy - h + hd);
        leftFace.addPoint(cx, cy + hd);
        leftFace.addPoint(cx - hw, cy);
        g2d.setColor(leftC);
        g2d.fill(leftFace);
        g2d.setColor(leftC.darker());
        g2d.draw(leftFace);

        // Wajah Kanan (Right Face)
        Polygon rightFace = new Polygon();
        rightFace.addPoint(cx, cy - h + hd);
        rightFace.addPoint(cx + hw, cy - h);
        rightFace.addPoint(cx + hw, cy);
        rightFace.addPoint(cx, cy + hd);
        g2d.setColor(rightC);
        g2d.fill(rightFace);
        g2d.setColor(rightC.darker());
        g2d.draw(rightFace);
    }

    /**
     * Menggambar Titik Api 3D:
     * Menghasilkan cahaya oranye dinamis di tanah (Radial Light Cast) dan lidah api vertikal berlapis.
     */
    public static void render3DFire(Graphics2D g2d, Fire fire) {
        int bx = (int) fire.getCenterX();
        int by = (int) fire.getCenterY();

        // 1. Pancaran Cahaya Oranye Hangat di Permukaan Tanah (Dynamic Ground Light Cast)
        g2d.setColor(new Color(255, 120, 0, 45));
        g2d.fillOval(bx - 36, by - 14, 72, 28);
        g2d.setColor(new Color(255, 180, 0, 75));
        g2d.fillOval(bx - 20, by - 8, 40, 16);

        // 2. Render struktur lidah api beranimasi
        fire.render(g2d);
    }

    /**
     * Menggambar Retikel Seleksi Isometrik 3D di Permukaan Tanah:
     * Lingkaran berlian isometrik yang memproyeksikan target mouse ke lantai hutan.
     */
    public static void render3DTargetReticle(Graphics2D g2d, int mx, int my, GameObject hoveredTarget) {
        if (mx < 0 || my < 0) return;

        int rx = mx;
        int ry = my;
        if (hoveredTarget != null) {
            rx = (int) hoveredTarget.getCenterX();
            ry = (int) hoveredTarget.getCenterY();
        }

        // Lingkaran berlian isometrik di lantai
        Polygon reticle = new Polygon();
        reticle.addPoint(rx, ry - 14);
        reticle.addPoint(rx + 28, ry);
        reticle.addPoint(rx, ry + 14);
        reticle.addPoint(rx - 28, ry);

        Color col = (hoveredTarget != null) ? new Color(231, 76, 60, 220) : new Color(52, 152, 219, 200);
        g2d.setColor(col);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.draw(reticle);

        // Garis bidik silang 3D
        g2d.drawLine(rx - 36, ry, rx + 36, ry);
        g2d.drawLine(rx, ry - 20, rx, ry + 20);
    }

    public static List<IsometricTree> getDecorativeTrees() {
        return DECORATIVE_TREES;
    }
}
