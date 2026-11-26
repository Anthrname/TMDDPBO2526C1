/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view;

import ecowarrior.config.GameConfig;
import ecowarrior.model.GameState;
import ecowarrior.view.dialogs.GameOverDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;
import javax.swing.JFrame;

/**
 * GameFrame - Window utama permainan ECO WARRIOR (View Layer - Pola MVP).
 * Menampung GamePanel sebagai kanvas visual dan meneruskan input keyboard ke presenter.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class GameFrame extends JFrame implements IGameView {

    private final GamePanel gamePanel;
    private Runnable onReturnToMenu;
    private Runnable onPauseToggle;
    private Runnable onMuteToggle;
    private Runnable onSaveAndQuit;

    public GameFrame() {
        setTitle(GameConfig.APP_TITLE + " — Bermain");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);
        getContentPane().add(gamePanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);

        // Registrasi Keyboard Listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE -> { if (onSaveAndQuit != null) onSaveAndQuit.run(); }
                    case KeyEvent.VK_P -> { if (onPauseToggle != null) onPauseToggle.run(); }
                    case KeyEvent.VK_M -> { if (onMuteToggle != null) onMuteToggle.run(); }
                }
            }
        });
        setFocusable(true);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setMouseClickHandler(BiConsumer<Integer, Integer> handler) {
        gamePanel.setMouseClickHandler(handler);
    }

    public void setOnReturnToMenu(Runnable r) { this.onReturnToMenu = r; }
    public void setOnPauseToggle(Runnable r) { this.onPauseToggle = r; }
    public void setOnMuteToggle(Runnable r) { this.onMuteToggle = r; }
    public void setOnSaveAndQuit(Runnable r) { this.onSaveAndQuit = r; }

    @Override
    public void renderState(GameState state) {
        gamePanel.setGameState(state);
        gamePanel.repaint();
    }

    @Override
    public void updateHUD(int achievement, int destroyer, int fire, double forestHealth, String rankName, int combo) {
        // HUD ditampilkan langsung di dalam GamePanel.drawHUD(), tidak butuh update terpisah
    }

    @Override
    public void showGameOverDialog(int score, int destroyer, int fire, boolean victory) {
        // Ambil data username dari game state via panel
        // Dialog ditampilkan dari GamePresenter yang memiliki akses ke GameState
    }

    public void showGameOverDialogFull(String username, int score, int destroyer, int fire, int totalScore, boolean victory) {
        GameOverDialog dialog = new GameOverDialog(this, username, score, destroyer, fire, totalScore, victory, onReturnToMenu);
        dialog.setVisible(true);
    }

    @Override
    public void closeGame() {
        dispose();
    }
}

