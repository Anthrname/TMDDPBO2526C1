package ecowarrior.assets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Mengelola daftar gambar (tiles + sprites) yang dimuat dari folder aset.
 *
 * <p>Gunakan {@link GameAssets#init(String)} (dari kelas bootstrap) agar gambar
 * terdaftar ke {@link Atlas}. Akses via {@link #get(String)} atau {@link #getMap()}.</p>
 */
public class TextureManager {

    private final Map<String, BufferedImage> atlas = new HashMap<>();

    /** Load semua file PNG di bawah {@code rootDir} (relatif ke working dir). */
    public void loadAll(String rootDir) {
        Path base = Paths.get(rootDir).toAbsolutePath().normalize();
        try (var stream = Files.walk(base)) {
            stream.filter(Files::isRegularFile)
                  .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".png"))
                  .forEach(p -> {
                      String key = base.relativize(p.toAbsolutePath().normalize())
                                 .toString().replace(File.separatorChar, '/');
                      try {
                          atlas.put(key, ImageIO.read(p.toFile()));
                      } catch (IOException e) {
                          System.err.println("[TextureManager] gagal baca " + key
                                      + " : " + e.getMessage());
                      }
                  });
        } catch (IOException e) {
            System.err.println("[TextureManager] gagal baca folder " + rootDir
                        + " : " + e.getMessage());
        }
    }

    public BufferedImage get(String key) {
        return atlas.get(key);
    }

    public Map<String, BufferedImage> getMap() {
        return atlas;
    }

    public boolean has(String key) {
        return atlas.containsKey(key);
    }

    public int count() {
        return atlas.size();
    }
}
