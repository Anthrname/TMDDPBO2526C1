/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.entity;

import ecowarrior.config.GameConfig;
import ecowarrior.model.RankTier;
import ecowarrior.assets.Atlas;
import ecowarrior.model.GameState;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Entitas Eco Warrior: Pion pembela lingkungan pelindung hutan dari kebakaran dan destroyer.
 * Dihasilkan dari Bunker Eco Warrior.
 * Dikendalikan melalui klik mouse: Eco Warrior terdekat akan merespons target yang diklik pemain.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class EcoWarrior extends GameObject {

    private GameObject targetEntity = null;
    private double manualTargetX = -1;
    private double manualTargetY = -1;

    private double shootCooldown = 0;
    private double patrolTimer = 0;
    private double patrolTargetX;
    private double patrolTargetY;

    private final RankTier rankTier;
    private double aimingAngle = 0;

    public EcoWarrior(double startX, double startY, RankTier rankTier) {
        super(startX, startY, 34, 34);
        this.rankTier = rankTier;
        pickPatrolPoint();
    }

    private void pickPatrolPoint() {
        this.patrolTargetX = 200 + Math.random() * (GameConfig.WINDOW_WIDTH - 300);
        this.patrolTargetY = 120 + Math.random() * (GameConfig.WINDOW_HEIGHT - 280);
        this.patrolTimer = 4.0 + Math.random() * 3.0;
    }

    /**
     * Menetapkan target penanganan (Destroyer ataupun Titik Api)
     * yang dipilih melalui klik mouse pemain.
     * 
     * @param target objek target yang akan ditangani
     */
    public void assignTarget(GameObject target) {
        this.targetEntity = target;
        this.manualTargetX = -1;
        this.manualTargetY = -1;
    }

    public void assignMoveTarget(double tx, double ty) {
        this.targetEntity = null;
        this.manualTargetX = tx;
        this.manualTargetY = ty;
    }

    @Override
    public void update(double dt) {
        if (shootCooldown > 0) {
            shootCooldown -= dt;
        }

        // Cek apakah target entitas masih aktif
        if (targetEntity != null && (!targetEntity.isActive())) {
            targetEntity = null;
        }

        double destX, destY;
        boolean hasActiveMission = false;

        if (targetEntity != null) {
            destX = targetEntity.getCenterX();
            destY = targetEntity.getCenterY();
            hasActiveMission = true;
            aimingAngle = Math.atan2(destY - getCenterY(), destX - getCenterX());
        } else if (manualTargetX >= 0 && manualTargetY >= 0) {
            destX = manualTargetX;
            destY = manualTargetY;
            hasActiveMission = true;
            aimingAngle = Math.atan2(destY - getCenterY(), destX - getCenterX());
            if (distanceTo(manualTargetX, manualTargetY) < 15) {
                manualTargetX = -1;
                manualTargetY = -1;
            }
        } else {
            // Patroli santai di sekitar hutan jika tidak ada target aktif
            patrolTimer -= dt;
            if (patrolTimer <= 0 || distanceTo(patrolTargetX, patrolTargetY) < 20) {
                pickPatrolPoint();
            }
            destX = patrolTargetX;
            destY = patrolTargetY;
            aimingAngle = Math.atan2(destY - getCenterY(), destX - getCenterX());
        }

        // Pergerakan menuju titik tujuan
        double dist = distanceTo(destX, destY);
        double speed = GameConfig.BASE_WARRIOR_SPEED * rankTier.getSpeedMultiplier();

        // Jika dalam jangkauan tembak target aktif, pelankan gerak untuk menembak
        if (hasActiveMission && targetEntity != null && dist <= GameConfig.WARRIOR_ATTACK_RANGE * 0.7) {
            speed *= 0.2; // Berhenti untuk memposisikan semprotan air
        }

        double angle = Math.atan2(destY - getCenterY(), destX - getCenterX());
        vx = Math.cos(angle) * speed;
        vy = Math.sin(angle) * speed;

        x += vx;
        y += vy;
    }

    /**
     * Memeriksa apakah Eco Warrior siap menembakkan air ke targetnya.
     * 
     * @return proyektil air jika berhasil menembak, null jika belum siap
     */
    public WaterProjectile attemptShootWater() {
        if (targetEntity == null || !targetEntity.isActive()) {
            return null;
        }

        double dist = distanceTo(targetEntity);
        if (dist <= GameConfig.WARRIOR_ATTACK_RANGE && shootCooldown <= 0) {
            shootCooldown = 0.55; // Interval tembakan
            double damage = 35.0 * rankTier.getExtinguishMultiplier();
            boolean splash = rankTier.isSplashUnlocked();

            return new WaterProjectile(
                    getCenterX(), getCenterY(),
                    targetEntity.getCenterX(), targetEntity.getCenterY(),
                    damage, splash, targetEntity
            );
        }
        return null;
    }

    public GameObject getTargetEntity() {
        return targetEntity;
    }

    public RankTier getRankTier() {
        return rankTier;
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!active) return;

        // Gambar karakter dari atlas (animasi idle / jalan).
        // Kunci: "eco_hero_anim" (registrasi di GameAssets.build)
        Atlas.INSTANCE.drawScreen(g2d, "eco_hero_anim",
                x, y, 0, (int) width, (int) height, (long) GameState.getGameTime());

        // Garis laser penargetan jika memiliki misi aktif
        if (targetEntity != null && targetEntity.isActive()) {
            g2d.setColor(new Color(52, 152, 219, 130));
            g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND, 1f, new float[]{6f, 6f}, 0f));
            g2d.drawLine((int) getCenterX(), (int) getCenterY(),
                    (int) targetEntity.getCenterX(), (int) targetEntity.getCenterY());
        }

        // Badge lambang daun rank di atas kepala
        if (rankTier != null) {
            g2d.setColor(rankTier.getBadgeColor());
            g2d.fillOval((int) (x + width / 2.0 - 4), (int) (y - 6), 8, 8);
        }
    }
}

