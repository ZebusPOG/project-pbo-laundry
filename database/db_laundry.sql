-- =============================================================
-- Database Aplikasi Laundry Kiloan Jatinangor
-- Jalankan sekali sebelum membuka aplikasi, contoh:
--   mysql -u root -p < database/db_laundry.sql
-- (atau buka file ini di phpMyAdmin / MySQL Workbench lalu Execute)
-- Aman dijalankan berulang: tabel dan data awal tidak dibuat dobel.
-- =============================================================
CREATE DATABASE IF NOT EXISTS db_laundry CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_laundry;

CREATE TABLE IF NOT EXISTS pelanggan (
    id_pelanggan INT AUTO_INCREMENT PRIMARY KEY,
    nama         VARCHAR(100) NOT NULL,
    no_hp        VARCHAR(20),
    alamat       VARCHAR(255),
    aktif        TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS layanan (
    id_layanan    INT AUTO_INCREMENT PRIMARY KEY,
    nama_layanan  VARCHAR(100) NOT NULL,
    harga_per_kg  DECIMAL(10,2) NOT NULL,
    estimasi_hari INT NOT NULL DEFAULT 2,
    aktif         TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS transaksi (
    id_transaksi  INT AUTO_INCREMENT PRIMARY KEY,
    id_pelanggan  INT NOT NULL,
    id_layanan    INT NOT NULL,
    berat_kg      DECIMAL(5,2) NOT NULL,
    harga_per_kg  DECIMAL(10,2) NOT NULL,
    total_harga   DECIMAL(12,2) NOT NULL,
    tanggal_masuk DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status        ENUM('DIPROSES','SELESAI','DIAMBIL') NOT NULL DEFAULT 'DIPROSES',
    FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id_pelanggan),
    FOREIGN KEY (id_layanan)   REFERENCES layanan(id_layanan)
);

-- Data awal layanan (hanya diisi jika tabel layanan masih kosong)
INSERT INTO layanan (nama_layanan, harga_per_kg, estimasi_hari)
SELECT awal.nama, awal.harga, awal.hari FROM (
    SELECT 'Cuci Kering'  AS nama, 5000 AS harga, 2 AS hari
    UNION ALL SELECT 'Cuci Setrika', 7000, 3
    UNION ALL SELECT 'Setrika Saja', 4000, 1
) AS awal
WHERE NOT EXISTS (SELECT 1 FROM layanan);
