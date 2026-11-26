package ecowarrior.assets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Bootstrap aset bagi ECO WARRIOR.
 *
 * <p>Fungsi {@link #build(TextureManager)} membaca gambar dari folder
 * {@code resources/images} dan mendaftarkannya ke {@code Atlas} dengan kunci
 * yang konsisten. Entity memanggil {@code Atlas.INSTANCE.draw(...)} dengan
 * kunci ini.</p>
 *
 * <p>Daftar kunci yang dipakai (lihat render entity di bawah ini):</p>
 * <ul>
 *   <li>{@code eco_hero_anim}  : animasi Eco Warrior (walk -> idle)</li>
 *   <li>{@code dest_hero_anim} : animasi Destroyer (walk -> idle)</li>
 *   <li>{@code fire_hero_anim} : perlapan api (ember)</li>
 *   <li>{@code water_shot}     : bom air proyektil</li>
 * </ul>
 */
public final class GameAssets {

    private static final TextureManager TM = new TextureManager();

    private GameAssets() {}

    /** Mulai memuat aset dari folder yang ditentukan. */
    public static void init(String assetRoot) {
        TM.loadAll(assetRoot);
        build(TM);
    }

    /** Membangun definisi atlas dari gambar yang sudah dimuat. */
    public static void build(TextureManager tm) {
        Map<String, BufferedImage> m = tm.getMap();

        // ---------- Eco Warrior ----------
        BufferedImage[] ecoFrames = {
                m.get("sprites/sprite_eco_walk_1.png"),
                m.get("sprites/sprite_eco_walk_2.png"),
                m.get("sprites/sprite_eco_walk_3.png"),
                m.get("sprites/sprite_eco_idle.png")
        };
        if (ecoFrames[0] != null && ecoFrames[1] != null && ecoFrames[2] != null && ecoFrames[3] != null) {
            Atlas.INSTANCE.addAnimated("eco_hero_anim", ecoFrames);
        } else {
            Atlas.INSTANCE.addNamed("eco_hero_anim", ecoFrames[0]);
        }

        // ---------- Destroyer ----------
        BufferedImage[] destFrames = {
                m.get("sprites/sprite_dest_walk_1.png"),
                m.get("sprites/sprite_dest_walk_2.png"),
                m.get("sprites/sprite_dest_walk_3.png"),
                m.get("sprites/sprite_dest_idle.png")
        };
        if (destFrames[0] != null && destFrames[1] != null && destFrames[2] != null && destFrames[3] != null) {
            Atlas.INSTANCE.addAnimated("dest_hero_anim", destFrames);
        } else {
            Atlas.INSTANCE.addNamed("dest_hero_anim", destFrames[0]);
        }

        // ---------- Fire ----------
        BufferedImage[] fireFrames = {
                m.get("sprites/sprite_fire_1.png"),
                m.get("sprites/sprite_fire_2.png"),
                m.get("sprites/sprite_fire_3.png")
        };
        if (fireFrames[0] != null && fireFrames[1] != null && fireFrames[2] != null) {
            Atlas.INSTANCE.addAnimated("fire_hero_anim", fireFrames);
        } else {
            Atlas.INSTANCE.addNamed("fire_hero_anim", fireFrames[0]);
        }

        // ---------- Water projectile ----------
        BufferedImage waterShot = m.get("sprites/sprite_water_1.png");
        if (waterShot != null) {
            Atlas.INSTANCE.addNamed("water_shot", waterShot);
        }
    }

    /** Kasar statistik loading aset (tidak ada di aplikasi final). */
    public static int assetCount() {
        return TM.count();
    }

    public static Map<String, BufferedImage> getMap() {
        return TM.getMap();
    }

    /** Helper kecil: ambil gambar dariTextureManager, fallback null. */
    public static BufferedImage get(String key) {
        return TM.get(key);
    }

    /** Path folder aset default: resources/images (relatif ke working dir). */
    public static String assetsRoot() {
        return "resources/images";
    }
}
