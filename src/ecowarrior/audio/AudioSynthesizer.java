/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * Pembangkit Audio Matematis Prosedural (AudioSynthesizer)
 * Menghasilkan instrumen musik BGM dan Sound Effect (SFX) 100% bebas royalti & legal
 * langsung dari perhitungan gelombang suara PCM 16-bit 44.1kHz.
 * 
 * Memenuhi syarat Bonus Nilai 20 tanpa risiko hak cipta dan tanpa dependensi eksternal!
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class AudioSynthesizer {

    private static final float SAMPLE_RATE = 44100.0f;

    /**
     * Membangkitkan BGM Menu bertema alam/lingkungan yang damai dan menenangkan.
     */
    public static byte[] generateMenuBGM() {
        int durationSec = 12;
        int totalSamples = (int) (SAMPLE_RATE * durationSec);
        byte[] audioData = new byte[totalSamples * 2];

        // Melodi tangga nada Pentatonik Alam (C4, D4, E4, G4, A4, C5)
        double[] freqs = {261.63, 293.66, 329.63, 392.00, 440.00, 523.25};
        int noteDuration = (int) (SAMPLE_RATE * 0.75);

        for (int i = 0; i < totalSamples; i++) {
            double t = (double) i / SAMPLE_RATE;
            int noteIndex = (i / noteDuration) % freqs.length;
            double freq = freqs[noteIndex];

            // Gelombang sinus halus dengan modulasi harmonik
            double sample = Math.sin(2 * Math.PI * freq * t) * 0.4
                          + Math.sin(2 * Math.PI * (freq * 2) * t) * 0.15
                          + Math.sin(2 * Math.PI * (freq * 0.5) * t) * 0.2;

            // Envelope per nada agar lembut (Attack - Decay)
            double notePhase = (double) (i % noteDuration) / noteDuration;
            double env = Math.sin(Math.PI * notePhase);
            sample *= env * 0.35;

            short val = (short) (sample * Short.MAX_VALUE);
            audioData[i * 2] = (byte) (val & 0xFF);
            audioData[i * 2 + 1] = (byte) ((val >> 8) & 0xFF);
        }

        return createWavBytes(audioData, 1, 16, (int) SAMPLE_RATE);
    }

    /**
     * Membangkitkan BGM Permainan bertema aksi heroik pejuang lingkungan yang dinamis.
     */
    public static byte[] generateGameBGM() {
        int durationSec = 16;
        int totalSamples = (int) (SAMPLE_RATE * durationSec);
        byte[] audioData = new byte[totalSamples * 2];

        // Pola progresi bass synth bersemangat
        double[] bassLine = {110.0, 110.0, 130.81, 146.83, 164.81, 146.83, 130.81, 123.47};
        int step = (int) (SAMPLE_RATE * 0.25);

        for (int i = 0; i < totalSamples; i++) {
            double t = (double) i / SAMPLE_RATE;
            int bassIdx = (i / (step * 2)) % bassLine.length;
            double bFreq = bassLine[bassIdx];

            // Bass synth
            double bass = (Math.sin(2 * Math.PI * bFreq * t) + 0.3 * Math.sin(4 * Math.PI * bFreq * t)) * 0.3;

            // Arpeggio nada tinggi
            double leadFreq = bFreq * 4;
            double arpHov = Math.sin(2 * Math.PI * (leadFreq * ((i / (step / 2)) % 3 + 1)) * t) * 0.12;

            // Ritme drum kick sintetis ringan
            int kickPhase = i % (step * 2);
            double kick = (kickPhase < 1200) ? Math.sin(2 * Math.PI * 65.0 * ((double) kickPhase / SAMPLE_RATE)) * 0.35 : 0;

            double sample = (bass + arpHov + kick) * 0.5;

            short val = (short) (sample * Short.MAX_VALUE);
            audioData[i * 2] = (byte) (val & 0xFF);
            audioData[i * 2 + 1] = (byte) ((val >> 8) & 0xFF);
        }

        return createWavBytes(audioData, 1, 16, (int) SAMPLE_RATE);
    }

    /**
     * SFX Tembakan Meriam Air (Water Shoot): Desisan semprotan air tekanan tinggi.
     */
    public static byte[] generateWaterShootSFX() {
        int durationSamples = (int) (SAMPLE_RATE * 0.22);
        byte[] audioData = new byte[durationSamples * 2];

        for (int i = 0; i < durationSamples; i++) {
            double progress = (double) i / durationSamples;
            double noise = (Math.random() * 2.0 - 1.0);
            double tone = Math.sin(2 * Math.PI * (420.0 - progress * 180.0) * ((double) i / SAMPLE_RATE));

            // Envelope cepat meredup
            double env = Math.pow(1.0 - progress, 2.0);
            double sample = (noise * 0.55 + tone * 0.45) * env * 0.65;

            short val = (short) (sample * Short.MAX_VALUE);
            audioData[i * 2] = (byte) (val & 0xFF);
            audioData[i * 2 + 1] = (byte) ((val >> 8) & 0xFF);
        }

        return createWavBytes(audioData, 1, 16, (int) SAMPLE_RATE);
    }

    /**
     * SFX Api Padam (Fire Extinguished): Suara uap desis 'shhh/hiss' ketika air menyentuh bara.
     */
    public static byte[] generateExtinguishSFX() {
        int durationSamples = (int) (SAMPLE_RATE * 0.45);
        byte[] audioData = new byte[durationSamples * 2];

        for (int i = 0; i < durationSamples; i++) {
            double progress = (double) i / durationSamples;
            double whiteNoise = (Math.random() * 2.0 - 1.0);
            double bubble = Math.sin(2 * Math.PI * (600.0 + Math.sin(progress * 40.0) * 120.0) * ((double) i / SAMPLE_RATE));

            double env = Math.sin(Math.PI * Math.sqrt(progress)) * (1.0 - progress);
            double sample = (whiteNoise * 0.75 + bubble * 0.25) * env * 0.7;

            short val = (short) (sample * Short.MAX_VALUE);
            audioData[i * 2] = (byte) (val & 0xFF);
            audioData[i * 2 + 1] = (byte) ((val >> 8) & 0xFF);
        }

        return createWavBytes(audioData, 1, 16, (int) SAMPLE_RATE);
    }

    /**
     * SFX Destroyer Dilumpuhkan (Destroyer Neutralized): Suara penangkapan / arrest pop bersemangat.
     */
    public static byte[] generateDestroyerHitSFX() {
        int durationSamples = (int) (SAMPLE_RATE * 0.35);
        byte[] audioData = new byte[durationSamples * 2];

        for (int i = 0; i < durationSamples; i++) {
            double progress = (double) i / durationSamples;
            double freq = 220.0 + progress * 580.0; // Frekuensi naik tanda keberhasilan
            double sample = Math.sin(2 * Math.PI * freq * ((double) i / SAMPLE_RATE)) * (1.0 - progress) * 0.65;

            short val = (short) (sample * Short.MAX_VALUE);
            audioData[i * 2] = (byte) (val & 0xFF);
            audioData[i * 2 + 1] = (byte) ((val >> 8) & 0xFF);
        }

        return createWavBytes(audioData, 1, 16, (int) SAMPLE_RATE);
    }

    /**
     * SFX Klik Tombol UI.
     */
    public static byte[] generateClickSFX() {
        int durationSamples = (int) (SAMPLE_RATE * 0.05);
        byte[] audioData = new byte[durationSamples * 2];

        for (int i = 0; i < durationSamples; i++) {
            double progress = (double) i / durationSamples;
            double sample = Math.sin(2 * Math.PI * 880.0 * ((double) i / SAMPLE_RATE)) * (1.0 - progress) * 0.4;
            short val = (short) (sample * Short.MAX_VALUE);
            audioData[i * 2] = (byte) (val & 0xFF);
            audioData[i * 2 + 1] = (byte) ((val >> 8) & 0xFF);
        }

        return createWavBytes(audioData, 1, 16, (int) SAMPLE_RATE);
    }

    /**
     * Membungkus raw PCM byte array menjadi format standar Audio WAV.
     */
    private static byte[] createWavBytes(byte[] pcmData, int channels, int sampleSizeBits, int sampleRate) {
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeBits, channels, true, false);
        ByteArrayInputStream bais = new ByteArrayInputStream(pcmData);
        AudioInputStream ais = new AudioInputStream(bais, format, pcmData.length / (channels * (sampleSizeBits / 8)));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, baos);
        } catch (IOException e) {
            System.err.println("[AudioSynthesizer] Gagal encode WAV: " + e.getMessage());
        }

        return baos.toByteArray();
    }
}

