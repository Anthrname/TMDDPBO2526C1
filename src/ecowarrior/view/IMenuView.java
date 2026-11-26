/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view;

import ecowarrior.model.Achievement;
import java.util.List;

/**
 * Interface IMenuView mendefinisikan kontrak tampilan menu utama sesuai pola MVP.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public interface IMenuView {
    void displayAchievements(List<Achievement> list);
    String getUsernameInput();
    void setUsernameInput(String username);
    void showMessage(String message, String title, int messageType);
    void setDatabaseStatus(String text, boolean isConnected);
    void setVisible(boolean visible);
    void dispose();
}

