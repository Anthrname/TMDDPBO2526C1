/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.model;

/**
 * Kelas Model Achievement merepresentasikan satu baris entitas pada tabel tachievement.
 * Menyimpan data riwayat pemain dan menghitung bonus tingkatan rank.
 * 
 * Sesuai spesifikasi tabel:
 * - username: nama user pemain
 * - destroyer: jumlah destroyer yang berhasil dilumpuhkan
 * - fire: jumlah titik api yang berhasil dipadamkan
 * - achievement: skor akumulatif dari destroyer dan fire
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class Achievement {
    private String username;
    private int destroyer;
    private int fire;
    private int achievement;

    public Achievement() {
        this("", 0, 0, 0);
    }

    public Achievement(String username, int achievement, int destroyer, int fire) {
        this.username = username;
        this.achievement = achievement;
        this.destroyer = destroyer;
        this.fire = fire;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDestroyer() {
        return destroyer;
    }

    public void setDestroyer(int destroyer) {
        this.destroyer = destroyer;
    }

    public int getFire() {
        return fire;
    }

    public void setFire(int fire) {
        this.fire = fire;
    }

    public int getAchievement() {
        return achievement;
    }

    public void setAchievement(int achievement) {
        this.achievement = achievement;
    }

    /**
     * Menambahkan hasil dari sesi permainan terkini ke data akumulatif pemain.
     * 
     * @param addedDestroyer jumlah destroyer dilumpuhkan di sesi ini
     * @param addedFire jumlah api dipadamkan di sesi ini
     * @param addedScore skor yang didapatkan di sesi ini
     */
    public void accumulateSession(int addedDestroyer, int addedFire, int addedScore) {
        this.destroyer += addedDestroyer;
        this.fire += addedFire;
        this.achievement += addedScore;
    }

    /**
     * Mengembalikan tingkatan rank berdasarkan total achievement yang terkumpul.
     * 
     * @return RankTier
     */
    public RankTier getRankTier() {
        return RankTier.fromScore(this.achievement);
    }

    @Override
    public String toString() {
        return String.format("Achievement[user=%s, score=%d, destroyer=%d, fire=%d, rank=%s]",
                username, achievement, destroyer, fire, getRankTier().getDisplayName());
    }
}

