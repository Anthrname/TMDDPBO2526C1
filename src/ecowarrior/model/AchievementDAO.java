/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) untuk tabel tachievement.
 * Mengelola seluruh operasi CRUD sesuai modul Model dalam pola MVP.
 * 
 * Aturan Bisnis Soal:
 * - Username dimasukkan jika belum ada di tabel tachievement saat tombol Play diklik.
 * - Jika sudah ada, tidak perlu dimasukkan ulang dan hanya memproses hasil achievement,
 *   destroyer, dan fire setelah permainan berakhir.
 * - Nilai achievement selalu bertambah setiap kali bermain.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class AchievementDAO {

    private final DatabaseConnection dbManager;
    private final List<Achievement> inMemoryFallbackList = new ArrayList<>();

    public AchievementDAO() {
        this.dbManager = DatabaseConnection.getInstance();
        if (!dbManager.isConnected()) {
            initFallbackData();
        }
    }

    private void initFallbackData() {
        inMemoryFallbackList.clear();
        inMemoryFallbackList.add(new Achievement("SaveTheTree", 1000, 3000, 100));
        inMemoryFallbackList.add(new Achievement("SaveTheCountry", 800, 1000, 80));
        inMemoryFallbackList.add(new Achievement("SaveTheEcosystem", 700, 7000, 40));
    }

    /**
     * Mengambil seluruh data achievement untuk ditampilkan pada JTable.
     * 
     * @return List objek Achievement diurutkan berdasarkan skor tertinggi (descending)
     */
    public List<Achievement> getAllAchievements() {
        List<Achievement> list = new ArrayList<>();
        Connection conn = dbManager.getConnection();

        if (conn != null) {
            String query = "SELECT username, achievement, destroyer, fire FROM tachievement ORDER BY achievement DESC;";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    Achievement ach = new Achievement(
                            rs.getString("username"),
                            rs.getInt("achievement"),
                            rs.getInt("destroyer"),
                            rs.getInt("fire")
                    );
                    list.add(ach);
                }
                return list;
            } catch (SQLException e) {
                System.err.println("[AchievementDAO] Kesalahan saat mengambil data: " + e.getMessage());
            }
        }

        // Jika koneksi gagal, kembalikan dari in-memory fallback
        return new ArrayList<>(inMemoryFallbackList);
    }

    /**
     * Mencari data achievement berdasarkan username.
     * 
     * @param username nama pemain
     * @return objek Achievement jika ditemukan, null jika belum ada
     */
    public Achievement findByUsername(String username) {
        Connection conn = dbManager.getConnection();
        if (conn != null) {
            String query = "SELECT username, achievement, destroyer, fire FROM tachievement WHERE LOWER(username) = LOWER(?);";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Achievement(
                                rs.getString("username"),
                                rs.getInt("achievement"),
                                rs.getInt("destroyer"),
                                rs.getInt("fire")
                        );
                    }
                }
            } catch (SQLException e) {
                System.err.println("[AchievementDAO] Kesalahan saat mencari username: " + e.getMessage());
            }
        }

        for (Achievement a : inMemoryFallbackList) {
            if (a.getUsername().equalsIgnoreCase(username)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Memeriksa apakah username sudah terdaftar di database.
     * 
     * @param username nama pemain
     * @return true jika sudah ada, false jika belum
     */
    public boolean userExists(String username) {
        return findByUsername(username) != null;
    }

    /**
     * Menyimpan username baru saat tombol Play diklik untuk pertama kalinya.
     * 
     * @param username nama pemain baru
     * @return true jika berhasil disimpan
     */
    public boolean insertNewUser(String username) {
        if (userExists(username)) {
            return false;
        }

        Connection conn = dbManager.getConnection();
        if (conn != null) {
            String query = "INSERT INTO tachievement (username, achievement, destroyer, fire) VALUES (?, 0, 0, 0);";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                int rows = pstmt.executeUpdate();
                return rows > 0;
            } catch (SQLException e) {
                System.err.println("[AchievementDAO] Gagal insert user baru: " + e.getMessage());
            }
        }

        // Simpan ke in-memory jika db offline
        inMemoryFallbackList.add(new Achievement(username, 0, 0, 0));
        return true;
    }

    /**
     * Mengakumulasikan dan memperbarui perolehan achievement setelah sesi permainan selesai.
     * 
     * @param username nama pemain
     * @param sessionDestroyer destroyer yang dilumpuhkan pada sesi ini
     * @param sessionFire api yang dipadamkan pada sesi ini
     * @param sessionScore skor yang diperoleh pada sesi ini
     * @return objek Achievement terbaru
     */
    public Achievement saveSessionStats(String username, int sessionDestroyer, int sessionFire, int sessionScore) {
        Achievement existing = findByUsername(username);

        if (existing == null) {
            // Jika user baru belum ada di tabel, insert dulu
            insertNewUser(username);
            existing = new Achievement(username, sessionScore, sessionDestroyer, sessionFire);
        } else {
            existing.accumulateSession(sessionDestroyer, sessionFire, sessionScore);
        }

        Connection conn = dbManager.getConnection();
        if (conn != null) {
            String updateQuery = "UPDATE tachievement SET achievement = ?, destroyer = ?, fire = ? WHERE LOWER(username) = LOWER(?);";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setInt(1, existing.getAchievement());
                pstmt.setInt(2, existing.getDestroyer());
                pstmt.setInt(3, existing.getFire());
                pstmt.setString(4, username);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("[AchievementDAO] Gagal memperbarui data: " + e.getMessage());
            }
        } else {
            // Update fallback list
            for (int i = 0; i < inMemoryFallbackList.size(); i++) {
                if (inMemoryFallbackList.get(i).getUsername().equalsIgnoreCase(username)) {
                    inMemoryFallbackList.set(i, existing);
                    break;
                }
            }
        }

        return existing;
    }

    public String getDatabaseInfo() {
        return dbManager.getDatabaseType();
    }

    public boolean isConnected() {
        return dbManager.isConnected();
    }
}

