-- =====================================================================
-- DATABASE SCHEMA & SEED DATA: ECO WARRIOR
-- Mata Kuliah : Desain dan Pemrograman Berorientasi Objek (DPBO)
-- Mahasiswa   : Najib Nurohman (NIM: 2509653) - Kelas C1
-- Dosen       : Rosa Ariani Sukamto
-- =====================================================================

CREATE DATABASE IF NOT EXISTS db_ecowarrior;
USE db_ecowarrior;

-- Buat tabel tachievement sesuai spesifikasi soal
DROP TABLE IF EXISTS tachievement;

CREATE TABLE tachievement (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    destroyer INT NOT NULL DEFAULT 0,
    fire INT NOT NULL DEFAULT 0,
    achievement INT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seed data awal persis sesuai spesifikasi contoh pada halaman 6 soal TMD
INSERT INTO tachievement (username, achievement, destroyer, fire) VALUES
('SaveTheTree', 1000, 3000, 100),
('SaveTheCountry', 800, 1000, 80),
('SaveTheEcosystem', 700, 7000, 40);

