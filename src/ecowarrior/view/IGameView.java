/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view;

import ecowarrior.model.GameState;

/**
 * Interface IGameView mendefinisikan kontrak tampilan layar permainan sesuai pola MVP.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public interface IGameView {
    void renderState(GameState state);
    void updateHUD(int achievement, int destroyer, int fire, double forestHealth, String rankName, int combo);
    void showGameOverDialog(int score, int destroyer, int fire, boolean victory);
    void closeGame();
}

