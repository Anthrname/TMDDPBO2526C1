/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.model;

import java.awt.Color;

/**
 * Enumerasi RankTier merepresentasikan tingkatan pencapaian (achievement)
 * yang memberikan bonus buff/efek nyata pada Eco Warrior di sesi permainan berikutnya.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public enum RankTier {
    RECRUIT("Ranger Recruit", 0, 1.0, 1.0, false, false, new Color(76, 175, 80)),
    GUARDIAN("Forest Guardian", 500, 1.25, 1.2, false, false, new Color(33, 150, 243)),
    HYDRO_SPECIALIST("Hydro Specialist", 1200, 1.5, 1.5, false, false, new Color(0, 188, 212)),
    ECO_COMMANDER("Eco Commander", 2500, 1.75, 1.8, true, false, new Color(255, 152, 0)),
    EARTH_LEGEND("Earth Legend", 5000, 2.0, 2.2, true, true, new Color(255, 215, 0));

    private final String displayName;
    private final int minScore;
    private final double speedMultiplier;
    private final double extinguishMultiplier;
    private final boolean splashUnlocked;
    private final boolean regenUnlocked;
    private final Color badgeColor;

    RankTier(String displayName, int minScore, double speedMultiplier, 
             double extinguishMultiplier, boolean splashUnlocked, 
             boolean regenUnlocked, Color badgeColor) {
        this.displayName = displayName;
        this.minScore = minScore;
        this.speedMultiplier = speedMultiplier;
        this.extinguishMultiplier = extinguishMultiplier;
        this.splashUnlocked = splashUnlocked;
        this.regenUnlocked = regenUnlocked;
        this.badgeColor = badgeColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMinScore() {
        return minScore;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public double getExtinguishMultiplier() {
        return extinguishMultiplier;
    }

    public boolean isSplashUnlocked() {
        return splashUnlocked;
    }

    public boolean isRegenUnlocked() {
        return regenUnlocked;
    }

    public Color getBadgeColor() {
        return badgeColor;
    }

    public static RankTier fromScore(int score) {
        if (score >= EARTH_LEGEND.minScore) return EARTH_LEGEND;
        if (score >= ECO_COMMANDER.minScore) return ECO_COMMANDER;
        if (score >= HYDRO_SPECIALIST.minScore) return HYDRO_SPECIALIST;
        if (score >= GUARDIAN.minScore) return GUARDIAN;
        return RECRUIT;
    }
}

