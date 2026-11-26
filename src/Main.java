/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

import ecowarrior.model.Achievement;
import ecowarrior.model.AchievementDAO;
import ecowarrior.model.GameState;
import ecowarrior.presenter.GamePresenter;
import ecowarrior.presenter.MenuPresenter;
import ecowarrior.view.GameFrame;
import ecowarrior.view.MenuFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main - Titik Masuk Program ECO WARRIOR.
 * 
 * Menginisialisasi seluruh komponen MVP (Model, View, Presenter) dan
 * mengorkestrasi transisi antar layar (Menu ↔ Game) secara mulus.
 * 
 * Arsitektur Pola MVP:
 * ┌────────────────────────────────────────────────────┐
 * │  MODEL              VIEW               PRESENTER   │
 * │  Achievement    ◄── MenuFrame     ◄── MenuPresenter │
 * │  AchievementDAO     GameFrame         GamePresenter │
 * │  GameState          GamePanel                       │
 * │  DatabaseConnection (IMenuView, IGameView interface)│
 * └────────────────────────────────────────────────────┘
 * 
 * @author Najib Nurohman (NIM: 2509653) - Kelas C1 DPBO 2026
 */
public class Main {

    public static void main(String[] args) {
        // Aktifkan Look and Feel Sistem agar GUI terlihat Native & Modern
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Lanjutkan dengan L&F default jika gagal
        }

        SwingUtilities.invokeLater(() -> {
            // Inisialisasi Model Tunggal (Singleton DAO) untuk seluruh siklus hidup aplikasi
            AchievementDAO dao = new AchievementDAO();

            // Inisialisasi View Menu
            MenuFrame menuFrame = new MenuFrame();

            // Inisialisasi Presenter Menu
            MenuPresenter menuPresenter = new MenuPresenter(menuFrame, dao);

            // Daftarkan callback "Play": Ketika tombol Play ditekan dan berhasil divalidasi
            menuPresenter.setOnPlayRequested(() -> {
                SwingUtilities.invokeLater(() -> {
                    String username = menuPresenter.getLastUsername();
                    Achievement baseAch = menuPresenter.getAchievementForUser(username);

                    // Sembunyikan menu, buka jendela game
                    menuFrame.setVisible(false);

                    GameFrame gameFrame = new GameFrame();
                    GameState gameState = new GameState(username, baseAch);

                    // Daftarkan callback "Return to Menu" dari layar Game
                    gameFrame.setOnReturnToMenu(() -> SwingUtilities.invokeLater(() -> {
                        gameFrame.dispose();
                        menuPresenter.onReturnedFromGame();
                        menuFrame.setVisible(true);
                    }));

                    // Inisialisasi Presenter Game
                    GamePresenter gamePresenter = new GamePresenter(gameFrame, dao, gameState, () -> {
                        // Callback ketika game benar-benar selesai (game over dari presenter)
                        SwingUtilities.invokeLater(() -> {
                            gameFrame.dispose();
                            menuPresenter.onReturnedFromGame();
                            menuFrame.setVisible(true);
                        });
                    });

                    gameFrame.setVisible(true);
                    gameFrame.requestFocusInWindow();

                    // Mulai game loop
                    gamePresenter.start();
                });
            });

            menuFrame.setVisible(true);
        });
    }
}

