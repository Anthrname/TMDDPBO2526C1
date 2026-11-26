/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.audio;

import java.io.ByteArrayInputStream;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Pengelola Audio (SoundManager) - Implementasi Fitur Bonus (Nilai 20).
 * Mengelola pemutaran musik latar (BGM), sound effect tembakan air, pemadaman api,
 * dan pengaturan volume suara (volume control / slider / mute).
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class SoundManager {

    private static SoundManager instance;

    private Clip bgmClip;
    private float volume = 0.75f; // Nilai 0.0 sampai 1.0
    private boolean muted = false;

    // Buffer audio sintesis siap pakai dalam memori
    private byte[] menuBgmBytes;
    private byte[] gameBgmBytes;
    private byte[] waterShootBytes;
    private byte[] extinguishBytes;
    private byte[] destroyerHitBytes;
    private byte[] clickBytes;

    private SoundManager() {
        initSounds();
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void initSounds() {
        // Generasi sinkron di sini (bukan thread background) agar byte audio
        // selalu tersedia sebelum playBGM / playSFX dipanggil. Ini menghilangkan
        // racecondition di mana BGM tidak berbunyi (bytes null) saat game mulai.
        menuBgmBytes = AudioSynthesizer.generateMenuBGM();
        gameBgmBytes = AudioSynthesizer.generateGameBGM();
        waterShootBytes = AudioSynthesizer.generateWaterShootSFX();
        extinguishBytes = AudioSynthesizer.generateExtinguishSFX();
        destroyerHitBytes = AudioSynthesizer.generateDestroyerHitSFX();
        clickBytes = AudioSynthesizer.generateClickSFX();
        System.out.println("[SoundManager] Inisialisasi audio sintesis legal selesai.");
    }

    public synchronized void playMenuBGM() {
        playBGMInternal(menuBgmBytes, "resources/sounds/bgm_menu.wav");
    }

    public synchronized void playGameBGM() {
        playBGMInternal(gameBgmBytes, "resources/sounds/bgm_game.wav");
    }

    private synchronized void playBGMInternal(byte[] synthesizedFallback, String customFilePath) {
        if (muted) return;

        stopBGM();

        new Thread(() -> {
            try {
                AudioInputStream ais = null;
                File customFile = new File(customFilePath);

                if (customFile.exists() && customFile.canRead()) {
                    ais = AudioSystem.getAudioInputStream(customFile);
                } else if (synthesizedFallback != null) {
                    ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(synthesizedFallback));
                }

                if (ais != null) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    applyVolume(clip, volume);
                    clip.start();

                    synchronized (SoundManager.this) {
                        bgmClip = clip;
                    }
                }
            } catch (Exception e) {
                System.err.println("[SoundManager] Gagal memutar BGM: " + e.getMessage());
            }
        }, "BGMPlaybackThread").start();
    }

    public synchronized void stopBGM() {
        if (bgmClip != null) {
            try {
                if (bgmClip.isRunning()) {
                    bgmClip.stop();
                }
                bgmClip.close();
            } catch (Exception ignored) {}
            bgmClip = null;
        }
    }

    public void playWaterShoot() {
        playSFXInternal(waterShootBytes, "resources/sounds/shoot.wav");
    }

    public void playExtinguish() {
        playSFXInternal(extinguishBytes, "resources/sounds/extinguish.wav");
    }

    public void playDestroyerHit() {
        playSFXInternal(destroyerHitBytes, "resources/sounds/destroyer_hit.wav");
    }

    public void playClick() {
        playSFXInternal(clickBytes, "resources/sounds/click.wav");
    }

    private void playSFXInternal(byte[] synthesizedFallback, String customFilePath) {
        if (muted || volume <= 0.02f) return;

        new Thread(() -> {
            try {
                AudioInputStream ais = null;
                File customFile = new File(customFilePath);

                if (customFile.exists() && customFile.canRead()) {
                    ais = AudioSystem.getAudioInputStream(customFile);
                } else if (synthesizedFallback != null) {
                    ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(synthesizedFallback));
                }

                if (ais != null) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    applyVolume(clip, volume);
                    clip.start();
                }
            } catch (Exception ignored) {}
        }, "SFXThread").start();
    }

    private void applyVolume(Clip clip, float vol) {
        if (clip == null) return;
        try {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                if (muted || vol <= 0.01f) {
                    gainControl.setValue(gainControl.getMinimum());
                } else {
                    float range = gainControl.getMaximum() - gainControl.getMinimum();
                    float gain = (range * vol) + gainControl.getMinimum();
                    // Batasi rentang aman decibel (-40 dB s.d. 0 dB)
                    gain = Math.max(-40.0f, Math.min(gainControl.getMaximum(), gain));
                    gainControl.setValue(gain);
                }
            }
        } catch (Exception ignored) {}
    }

    public synchronized void setVolume(float newVolume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, newVolume));
        if (bgmClip != null && bgmClip.isOpen()) {
            applyVolume(bgmClip, this.volume);
        }
    }

    public float getVolume() {
        return volume;
    }

    public synchronized void setMuted(boolean muted) {
        this.muted = muted;
        if (muted) {
            if (bgmClip != null && bgmClip.isRunning()) {
                bgmClip.stop();
            }
        } else {
            if (bgmClip != null && !bgmClip.isRunning()) {
                applyVolume(bgmClip, volume);
                bgmClip.start();
            }
        }
    }

    public boolean isMuted() {
        return muted;
    }

    public synchronized void toggleMute() {
        setMuted(!muted);
    }
}

