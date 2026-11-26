/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.config;

import java.awt.Color;
import java.awt.Font;

/**
 * Kelas GameConfig menyimpan seluruh konstanta konfigurasi global game ECO WARRIOR.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public final class GameConfig {

    private GameConfig() {
        // Mencegah instansiasi class utilitas
    }

    // Informasi Mahasiswa & Proyek
    public static final String STUDENT_NAME = "Najib Nurohman";
    public static final String STUDENT_NIM = "2509653";
    public static final String STUDENT_CLASS = "C1";
    public static final String APP_TITLE = "ECO WARRIOR - Forest Protection Simulator";
    public static final String VERSION = "1.0.0 Pro Edition";

    // Dimensi Layar
    public static final int WINDOW_WIDTH = 1080;
    public static final int WINDOW_HEIGHT = 720;
    public static final int FPS = 60;
    public static final long TARGET_TIME = 1000 / FPS;

    // Nilai Poin & Penghitungan Achievement
    public static final int SCORE_PER_DESTROYER = 50;
    public static final int SCORE_PER_FIRE = 25;
    public static final int SCORE_COMBO_BONUS = 15;

    // Keseimbangan Gameplay
    public static final int INITIAL_FOREST_HEALTH = 100;
    public static final double FIRE_HEALTH_DECAY_RATE = 0.08; // Pengurangan kesehatan hutan per api per detik
    public static final int MAX_DESTROYERS = 12;
    public static final int MAX_ECO_WARRIORS = 8;
    public static final int MAX_FIRES = 20;

    // Kecepatan & Durasi
    public static final double BASE_WARRIOR_SPEED = 2.4;
    public static final double BASE_DESTROYER_SPEED = 1.6;
    public static final double PROJECTILE_SPEED = 7.0;
    public static final double WARRIOR_ATTACK_RANGE = 260.0;

    // Palet Warna UI Modern
    public static final Color COLOR_PRIMARY_DARK = new Color(18, 28, 22);
    public static final Color COLOR_PANEL_BG = new Color(26, 38, 30);
    public static final Color COLOR_ACCENT_GREEN = new Color(46, 204, 113);
    public static final Color COLOR_ACCENT_HOVER = new Color(39, 174, 96);
    public static final Color COLOR_ACCENT_BLUE = new Color(52, 152, 219);
    public static final Color COLOR_ACCENT_RED = new Color(231, 76, 60);
    public static final Color COLOR_ACCENT_ORANGE = new Color(230, 126, 34);
    public static final Color COLOR_TEXT_LIGHT = new Color(240, 248, 245);
    public static final Color COLOR_TEXT_MUTED = new Color(160, 185, 170);

    // Tipografi Font
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 28);
    public static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_REGULAR = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_HUD = new Font("SansSerif", Font.BOLD, 16);
}

