/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.presenter;

import ecowarrior.audio.SoundManager;
import ecowarrior.model.Achievement;
import ecowarrior.model.AchievementDAO;
import ecowarrior.view.MenuFrame;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * MenuPresenter - Lapisan Logika Menu Utama (Presenter Layer - Pola MVP).
 * Memediasi antara MenuFrame (View) dan AchievementDAO (Model).
 * View tidak pernah berinteraksi langsung dengan Model.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class MenuPresenter {

    private final MenuFrame view;
    private final AchievementDAO dao;
    private Runnable onPlayRequested;

    private String lastUsername = "";

    public MenuPresenter(MenuFrame view, AchievementDAO dao) {
        this.view = view;
        this.dao = dao;
        attachListeners();
        updateDatabaseStatus();
        refreshLeaderboard();
        SoundManager.getInstance().playMenuBGM();
    }

    private void attachListeners() {
        view.setPlayListener(e -> handlePlay());
        view.setRefreshListener(e -> refreshLeaderboard());
    }

    private void handlePlay() {
        SoundManager.getInstance().playClick();
        String username = view.getUsernameInput();

        if (username == null || username.isBlank()) {
            view.showMessage(
                "Harap masukkan username terlebih dahulu sebelum bermain.",
                "Username Diperlukan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (username.length() < 3 || username.length() > 50) {
            view.showMessage(
                "Username harus antara 3 hingga 50 karakter.",
                "Format Username Salah",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Jika username belum ada, insert otomatis ke tabel tachievement
        boolean isNewUser = !dao.userExists(username);
        if (isNewUser) {
            dao.insertNewUser(username);
            view.showMessage(
                "Selamat datang, " + username + "! Akun baru Anda telah didaftarkan.\n" +
                "Jaga kelestarian hutan dan kumpulkan achievement sebanyak-banyaknya!",
                "Pemain Baru Terdaftar",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        // Ambil data achievement untuk diteruskan ke game
        Achievement ach = dao.findByUsername(username);
        view.triggerRankPreviewUpdate(ach);

        this.lastUsername = username;

        // Perintahkan aplikasi untuk memulai game
        if (onPlayRequested != null) {
            onPlayRequested.run();
        }
    }

    private void refreshLeaderboard() {
        SoundManager.getInstance().playClick();
        new Thread(() -> {
            List<Achievement> list = dao.getAllAchievements();
            SwingUtilities.invokeLater(() -> {
                view.displayAchievements(list);

                // Perbarui preview rank jika sudah ada username diketik
                String currentUsername = view.getUsernameInput();
                if (!currentUsername.isBlank()) {
                    Achievement ach = null;
                    for (Achievement a : list) {
                        if (a.getUsername().equalsIgnoreCase(currentUsername)) {
                            ach = a;
                            break;
                        }
                    }
                    view.triggerRankPreviewUpdate(ach);
                }
            });
        }, "RefreshThread").start();
    }

    private void updateDatabaseStatus() {
        new Thread(() -> {
            boolean connected = dao.isConnected();
            String info = dao.getDatabaseInfo();
            SwingUtilities.invokeLater(() -> view.setDatabaseStatus(info, connected));
        }, "DBStatusThread").start();
    }

    public String getLastUsername() {
        return lastUsername;
    }

    public Achievement getAchievementForUser(String username) {
        return dao.findByUsername(username);
    }

    public void onReturnedFromGame() {
        SoundManager.getInstance().playMenuBGM();
        refreshLeaderboard();
    }

    public void setOnPlayRequested(Runnable callback) {
        this.onPlayRequested = callback;
    }
}

