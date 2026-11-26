# ECO WARRIOR 🌿🔥
### Forest Protection Simulator — Tugas Masa Depan (TMD) DPBO 2026

<div align="center">

```
🌲  E C O  W A R R I O R  🌲
     vs
💥  D E S T R O Y E R  💥
```

| Item | Detail |
|------|--------|
| **Mahasiswa** | Najib Nurohman |
| **NIM** | 2509653 |
| **Kelas** | C1 |
| **Dosen** | Rosa Ariani Sukamto |
| **Mata Kuliah** | Desain dan Pemrograman Berorientasi Objek (DPBO) |
| **Institusi** | Ilmu Komputer FPMIPA — Universitas Pendidikan Indonesia |
| **Tahun** | 2026 |

</div>

---

## 🎮 Deskripsi Program

**ECO WARRIOR** adalah game simulasi pertahanan ekosistem hutan bergenre *Tower Defense* yang dibangun sepenuhnya menggunakan Java Swing dengan arsitektur **MVP (Model–View–Presenter)** dan **Game Loop berbasis Thread 60 FPS**.

Pemain berperan sebagai komandan Eco Warrior: mengklik unit Destroyer atau Titik Api untuk menugaskan Eco Warrior terdekat memadamkan api dan melumpuhkan perusak hutan. Seluruh statistik (achievement, destroyer, fire) tersimpan ke database MySQL secara akumulatif setiap sesi.

---

## 📋 Pemenuhan Spesifikasi Soal

### ✅ Fitur Wajib

| No. | Spesifikasi | Status | Detail Implementasi |
|-----|-------------|--------|---------------------|
| 1 | **JFrame Menu Utama** | ✅ | `MenuFrame.java` — Header identitas, JTable, input username, tombol Play |
| 2 | **JTable tachievement** | ✅ | Menampilkan username, achievement, destroyer, fire dari DB |
| 3 | **Input username sebelum bermain** | ✅ | JTextField dengan validasi (3–50 karakter) |
| 4 | **Simpan username baru ke DB** | ✅ | Auto INSERT jika username belum ada di tachievement |
| 5 | **Layar Permainan** | ✅ | `GameFrame + GamePanel` — 60 FPS double-buffered rendering |
| 6 | **Bunker Destroyer (kiri bawah)** | ✅ | `Bunker.java` bertipe `BunkerType.DESTROYER` — spawn otomatis Destroyer |
| 7 | **Bunker Eco Warrior (kanan bawah)** | ✅ | `Bunker.java` bertipe `BunkerType.ECO_WARRIOR` — spawn otomatis Warrior |
| 8 | **Pion Destroyer** | ✅ | `Destroyer.java` — Bergerak, menyalakan api, ada blood bar |
| 9 | **Pion Eco Warrior** | ✅ | `EcoWarrior.java` — Patroli, respons klik, tembakkan air dengan cooldown |
| 10 | **Titik Api** | ✅ | `Fire.java` — Animasi flame bergelombang, partikel bara api |
| 11 | **Kontrol klik mouse** | ✅ | Eco Warrior terdekat otomatis diberi target saat klik mouse |
| 12 | **Keluar dengan SPACE** | ✅ | Tersimpan ke DB, tampil GameOverDialog, kembali ke menu |
| 13 | **Simpan ke DB saat keluar** | ✅ | `AchievementDAO.saveSessionStats()` — akumulatif setiap sesi |
| 14 | **Tabel MySQL tachievement** | ✅ | `DatabaseConnection.java` — MySQL auto-create + SQLite fallback |
| 15 | **Pola OOP Lengkap** | ✅ | Abstraction, Inheritance, Polymorphism, Encapsulation |

### ✅ Bonus Nilai (Nilai +20)

| No. | Bonus | Status | Implementasi |
|-----|-------|--------|--------------|
| B1 | **Musik & Sound Effect** | ✅ | `AudioSynthesizer.java` + `SoundManager.java` — BGM Menu, BGM Game, 4 SFX (tembakan air, padamkan api, Destroyer lumpuh, klik UI) |
| B2 | **Pengaturan Volume Suara** | ✅ | `SoundControlPanel.java` — JSlider volume real-time (0–100%), tombol Mute/Unmute di menu |
| B3 | **Sistem Rank Achievement** | ✅ | `RankTier.java` (enum) — 5 tingkat rank dengan buff nyata pada gameplay sesi berikutnya |
| B4 | **Efek Partikel Visual** | ✅ | `Particle.java` — Percikan air, uap api padam, bara terbang, teks floating score |
| B5 | **Fallback SQLite** | ✅ | `DatabaseConnection.java` — Auto-switch ke SQLite lokal jika MySQL offline |
| B6 | **Combo Multiplier** | ✅ | Poin berganda saat berhasil melumpuhkan beruntun dalam 3,5 detik |

---

## 🗂️ Struktur Proyek & Arsitektur

```
TMD/
├── src/
│   ├── Main.java                          # Entry Point + Orkestrasi MVP
│   └── ecowarrior/
│       ├── audio/
│       │   ├── AudioSynthesizer.java      # Pembangkit BGM + SFX matematis
│       │   └── SoundManager.java          # Pengelola audio (Singleton)
│       ├── config/
│       │   └── GameConfig.java            # Semua konstanta game
│       ├── entity/                        # Entitas dunia game
│       │   ├── GameObject.java            # Kelas Abstrak induk (OOP)
│       │   ├── Bunker.java                # Markas penghasil unit
│       │   ├── BunkerType.java            # Enum tipe bunker
│       │   ├── Destroyer.java             # Pion perusak hutan
│       │   ├── EcoWarrior.java            # Pion pejuang lingkungan
│       │   ├── Fire.java                  # Titik api beranimasi
│       │   ├── WaterProjectile.java       # Proyektil air
│       │   └── Particle.java              # Partikel efek visual
│       ├── model/                         # Lapisan MODEL (MVP)
│       │   ├── Achievement.java           # Entitas data tachievement
│       │   ├── AchievementDAO.java        # Operasi CRUD database
│       │   ├── DatabaseConnection.java    # Koneksi MySQL + fallback SQLite
│       │   ├── GameState.java             # State seluruh dunia game
│       │   └── RankTier.java              # Enum tingkatan rank + buff
│       ├── view/                          # Lapisan VIEW (MVP)
│       │   ├── IMenuView.java             # Interface kontrak menu
│       │   ├── IGameView.java             # Interface kontrak game
│       │   ├── MenuFrame.java             # Jendela menu utama
│       │   ├── GameFrame.java             # Jendela permainan
│       │   ├── GamePanel.java             # Kanvas rendering 60FPS
│       │   ├── components/
│       │   │   ├── ModernButton.java      # Tombol kustom animasi hover
│       │   │   ├── ModernTable.java       # JTable dengan zebra stripes
│       │   │   └── SoundControlPanel.java # Slider volume + tombol mute
│       │   └── dialogs/
│       │       ├── AboutDialog.java       # Dialog info & janji akademik
│       │       ├── HowToPlayDialog.java   # Panduan bermain & buff sistem
│       │       └── GameOverDialog.java    # Dialog hasil akhir game
│       └── presenter/                     # Lapisan PRESENTER (MVP)
│           ├── MenuPresenter.java         # Logika menu utama
│           └── GamePresenter.java         # Game loop + collision + logika game
├── database/
│   └── db_ecowarrior.sql                  # Script SQL buat tabel + seed data
├── lib/
│   ├── mysql-connector-j-8.3.0.jar        # Driver MySQL JDBC
│   └── sqlite-jdbc-3.45.1.0.jar           # Driver SQLite JDBC (fallback)
├── resources/
│   └── sounds/                            # (Opsional) file audio kustom .wav
├── EcoWarrior.jar                         # File JAR executable
├── run.bat                                # Script jalankan langsung
├── compile_and_run.bat                    # Script kompilasi + jalankan
└── manifest.txt                           # Manifest JAR
```

---

## 🚀 Cara Menjalankan Program

### Prasyarat
- **Java JDK 17+** (sudah tersedia di mesin)
- **XAMPP** (MySQL) — Opsional, program berjalan mulus dengan SQLite jika MySQL offline

### Opsi 1: Jalankan JAR (Paling Mudah)
```bash
# Klik dua kali file run.bat
# atau jalankan dari terminal:
java -cp "EcoWarrior.jar;lib\mysql-connector-j-8.3.0.jar;lib\sqlite-jdbc-3.45.1.0.jar" Main
```

### Opsi 2: Compile dari Source
```bash
# Klik dua kali compile_and_run.bat
# atau jalankan dari terminal:
javac -encoding UTF-8 -d out -cp "src;lib\mysql-connector-j-8.3.0.jar;lib\sqlite-jdbc-3.45.1.0.jar" src\Main.java
java -cp "out;lib\mysql-connector-j-8.3.0.jar;lib\sqlite-jdbc-3.45.1.0.jar" Main
```

### Setup Database MySQL (Opsional)
```sql
-- Buka XAMPP → Start MySQL → buka phpMyAdmin atau terminal:
SOURCE database/db_ecowarrior.sql;
-- Program akan otomatis mendeteksi dan terhubung ke MySQL!
```

---

## 🎮 Panduan Bermain

| Tombol / Aksi | Fungsi |
|--------------|--------|
| **KLIK MOUSE** pada Destroyer 💥 | Eco Warrior terdekat menyerang target |
| **KLIK MOUSE** pada Titik Api 🔥 | Eco Warrior terdekat memadamkan api |
| **KLIK MOUSE** di area kosong | Eco Warrior terdekat berpindah lokasi |
| **SPACE** | Simpan sesi ke DB → kembali ke Menu |
| **P** | Jeda / Lanjutkan permainan |
| **M** | Mute / Unmute audio |

---

## 🏆 Sistem Rank Achievement

Skor achievement terakumulasi di database dan memberikan **buff nyata** pada sesi permainan berikutnya:

| Rank | Achievement Minimum | Speed | Extinguish | Splash | Regen Hutan |
|------|---------------------|-------|------------|--------|-------------|
| 🌱 Ranger Recruit | 0 pts | ×1.0 | ×1.0 | ✗ | ✗ |
| 💙 Forest Guardian | 500 pts | ×1.25 | ×1.2 | ✗ | ✗ |
| 💧 Hydro Specialist | 1200 pts | ×1.5 | ×1.5 | ✗ | ✗ |
| 🔶 Eco Commander | 2500 pts | ×1.75 | ×1.8 | ✅ Area | ✗ |
| 🌟 Earth Legend | 5000 pts | ×2.0 | ×2.2 | ✅ Area | ✅ Aktif |

---

## 🎵 Sistem Audio (Fitur Bonus Nilai 20)

Seluruh suara dihasilkan secara **prosedural matematis** (Audio Synthesis PCM 44.1kHz 16-bit) tanpa file audio eksternal dan **100% bebas lisensi/royalti**:

- 🎵 **BGM Menu** — Melodi Pentatonik Alam yang menenangkan
- 🎮 **BGM Game** — Ritme arpeggio sintetis bersemangat
- 💧 **SFX Tembakan Air** — Desisan semprot air tekanan tinggi  
- 🌊 **SFX Api Padam** — Suara uap sizzling saat air menyentuh bara
- ✅ **SFX Destroyer Lumpuh** — Nada kemenangan meninggi
- 🖱️ **SFX Klik UI** — Klik tombol bersih

**Kontrol Volume**: JSlider di menu utama untuk mengatur volume real-time + tombol Mute/Unmute.

---

## ⚙️ Prinsip OOP yang Diterapkan

| Prinsip | Implementasi |
|---------|-------------|
| **Abstraction** | `GameObject` (abstract class) sebagai cetak biru semua entitas |
| **Inheritance** | `EcoWarrior`, `Destroyer`, `Fire`, `Bunker` extends `GameObject` |
| **Polymorphism** | Method `update(dt)` dan `render(g2d)` di-override tiap entitas |
| **Encapsulation** | Semua field `private` / `protected`, akses via getter-setter |
| **Interface** | `IMenuView`, `IGameView` mendefinisikan kontrak View di MVP |
| **Enum** | `RankTier`, `BunkerType`, `Particle.Type` untuk tipe terdefinisi |
| **Singleton** | `DatabaseConnection`, `SoundManager` — satu instance global |
| **DAO Pattern** | `AchievementDAO` memisahkan logika SQL dari bisnis game |
| **MVP Pattern** | Model ↔ Presenter ↔ View tanpa coupling langsung |

---

## 🗄️ Skema Database

```sql
CREATE TABLE tachievement (
    username    VARCHAR(50) NOT NULL PRIMARY KEY,
    destroyer   INT NOT NULL DEFAULT 0,   -- Total destroyer dilumpuhkan
    fire        INT NOT NULL DEFAULT 0,   -- Total api dipadamkan
    achievement INT NOT NULL DEFAULT 0    -- Total skor akumulatif
);
```

**Seed data default** (sesuai spesifikasi soal halaman 6):

| username | achievement | destroyer | fire |
|----------|------------|-----------|------|
| SaveTheTree | 1000 | 3000 | 100 |
| SaveTheCountry | 800 | 1000 | 80 |
| SaveTheEcosystem | 700 | 7000 | 40 |

---

## 📜 Janji Integritas Akademik

> *"Saya Najib Nurohman sebagai manusia yang berTuhan, saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin."*

---

<div align="center">

**ECO WARRIOR v1.0.0 Pro Edition**  
*Jaga kelestarian hutan, bangun peradaban berkelanjutan!* 🌍

</div>

