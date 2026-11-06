# Proyek PBO: Aplikasi Laundry Kiloan Jatinangor

**Mata kuliah:** Praktikum Pemrograman Berbasis Objek
**Tahap:** Minggu 1 (2 - 6 November 2026): ide proyek, gambaran aplikasi, rancangan database, rancangan class
**Kelompok:** (isi nama anggota, NIM, kelas)

---

## 1. Ide Proyek

### 1.1 Latar Belakang
Jatinangor dihuni banyak mahasiswa, sehingga usaha laundry kiloan sangat banyak dan permintaannya stabil. Banyak UMKM laundry masih mencatat order secara manual di buku nota. Pencatatan manual rawan masalah:
- nota hilang atau tulisan tidak terbaca,
- total harga salah hitung,
- sulit mengecek cucian milik siapa yang sudah selesai atau belum diambil,
- sulit merekap pemasukan.

### 1.2 Solusi
Aplikasi desktop berbasis **Java Swing** yang terhubung ke **database MySQL** (lewat JDBC) untuk mencatat pelanggan, daftar layanan, dan transaksi laundry, termasuk status pengerjaan cucian.

### 1.3 Tujuan
1. Mencatat data pelanggan dan layanan secara rapi.
2. Menghitung total biaya otomatis (berat x harga per kg).
3. Memantau status cucian: Diproses, Selesai, Diambil.
4. Menampilkan riwayat transaksi yang bisa dicari dan difilter.

---

## 2. Gambaran Aplikasi

### 2.1 Deskripsi Singkat
Aplikasi digunakan oleh **pemilik/kasir laundry** (satu jenis pengguna) untuk mengelola data master dan transaksi harian. Aplikasi memiliki satu jendela utama dengan tiga menu/tab: **Pelanggan**, **Layanan**, dan **Transaksi**.

### 2.2 Daftar Fitur

| No | Fitur | Keterangan |
|----|-------|------------|
| 1 | Kelola Pelanggan | Tambah, lihat, cari, ubah, hapus pelanggan |
| 2 | Kelola Layanan | Tambah, lihat, ubah, hapus jenis layanan dan harga per kg |
| 3 | Input Transaksi | Pilih pelanggan dan layanan, input berat, total dihitung otomatis |
| 4 | Riwayat Transaksi | Tabel semua transaksi, bisa dicari dan difilter per status |
| 5 | Ubah Status | Ubah status cucian: Diproses, Selesai, Diambil |
| 6 | Batalkan Transaksi | Hapus transaksi yang salah input |
| 7 | Ringkasan (opsional) | Total pemasukan dan jumlah cucian per status |

### 2.3 Alur Penggunaan

**Alur utama (order baru):**
1. Kasir membuka aplikasi, lalu masuk ke menu **Transaksi**.
2. Kasir memilih pelanggan dari `JComboBox`. Jika pelanggan baru, kasir pindah dulu ke menu **Pelanggan** untuk menambahkannya.
3. Kasir memilih layanan dari `JComboBox`. Harga per kg tampil otomatis.
4. Kasir mengisi berat cucian (kg). Total harga langsung terhitung.
5. Kasir menekan **Simpan**. Transaksi tersimpan dengan status **Diproses** dan muncul di tabel riwayat.
6. Ketika cucian selesai, kasir memilih baris transaksi lalu menekan **Tandai Selesai**.
7. Ketika pelanggan mengambil cucian, kasir menekan **Tandai Diambil**.

**Alur kelola data master:**
- Pilih menu Pelanggan/Layanan, lalu isi form dan tekan Simpan. Data muncul di `JTable`.
- Pilih baris di tabel, data tampil di form, lalu tekan **Ubah** atau **Hapus**.
- Data yang sudah dipakai di transaksi tidak dihapus permanen, tetapi dinonaktifkan (kolom `aktif = 0`) agar riwayat transaksi tetap utuh.

### 2.4 Rencana Tampilan (Layar)

| Layar | Komponen Swing utama |
|-------|----------------------|
| Jendela utama | `JFrame` + `JTabbedPane` (tab Pelanggan, Layanan, Transaksi) |
| Tab Pelanggan | `JTextField` (nama, no HP), `JTextArea` (alamat), tombol Simpan/Ubah/Hapus/Reset, `JTextField` pencarian, `JTable` |
| Tab Layanan | `JTextField` (nama layanan, harga per kg, estimasi hari), tombol Simpan/Ubah/Hapus/Reset, `JTable` |
| Tab Transaksi | `JComboBox` pelanggan dan layanan, `JTextField` berat, label total, tombol Simpan, `JComboBox` filter status, tombol Tandai Selesai/Diambil/Batalkan, `JTable` riwayat |

---

## 3. Rancangan Database

**Nama database:** `db_laundry`
**Jumlah tabel:** 3 (`pelanggan`, `layanan`, `transaksi`)

### 3.1 Relasi Antar Tabel (ERD)

```
 pelanggan (1) ──────< (N) transaksi (N) >────── (1) layanan
```

- Satu pelanggan dapat memiliki banyak transaksi.
- Satu layanan dapat dipakai di banyak transaksi.
- Setiap transaksi dimiliki satu pelanggan dan memakai satu layanan.

### 3.2 Struktur Tabel

**Tabel `pelanggan`**

| Kolom | Tipe | Keterangan |
|-------|------|------------|
| id_pelanggan | INT, PK, AUTO_INCREMENT | Identitas pelanggan |
| nama | VARCHAR(100), NOT NULL | Nama pelanggan |
| no_hp | VARCHAR(20) | Nomor HP |
| alamat | VARCHAR(255) | Alamat/kos |
| aktif | TINYINT(1), default 1 | 1 = aktif, 0 = dinonaktifkan |

**Tabel `layanan`**

| Kolom | Tipe | Keterangan |
|-------|------|------------|
| id_layanan | INT, PK, AUTO_INCREMENT | Identitas layanan |
| nama_layanan | VARCHAR(100), NOT NULL | Contoh: Cuci Setrika |
| harga_per_kg | DECIMAL(10,2), NOT NULL | Harga per kilogram |
| estimasi_hari | INT, default 2 | Perkiraan lama pengerjaan |
| aktif | TINYINT(1), default 1 | 1 = aktif, 0 = dinonaktifkan |

**Tabel `transaksi`**

| Kolom | Tipe | Keterangan |
|-------|------|------------|
| id_transaksi | INT, PK, AUTO_INCREMENT | Identitas transaksi |
| id_pelanggan | INT, FK -> pelanggan | Pemilik cucian |
| id_layanan | INT, FK -> layanan | Layanan yang dipilih |
| berat_kg | DECIMAL(5,2), NOT NULL | Berat cucian |
| harga_per_kg | DECIMAL(10,2), NOT NULL | Harga saat transaksi (salinan, supaya riwayat tidak berubah bila harga layanan diubah) |
| total_harga | DECIMAL(12,2), NOT NULL | berat_kg x harga_per_kg |
| tanggal_masuk | DATETIME, default CURRENT_TIMESTAMP | Waktu order dibuat |
| status | ENUM('DIPROSES','SELESAI','DIAMBIL'), default 'DIPROSES' | Status cucian |

### 3.3 Script SQL

```sql
CREATE DATABASE IF NOT EXISTS db_laundry;
USE db_laundry;

CREATE TABLE pelanggan (
    id_pelanggan INT AUTO_INCREMENT PRIMARY KEY,
    nama         VARCHAR(100) NOT NULL,
    no_hp        VARCHAR(20),
    alamat       VARCHAR(255),
    aktif        TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE layanan (
    id_layanan    INT AUTO_INCREMENT PRIMARY KEY,
    nama_layanan  VARCHAR(100) NOT NULL,
    harga_per_kg  DECIMAL(10,2) NOT NULL,
    estimasi_hari INT NOT NULL DEFAULT 2,
    aktif         TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE transaksi (
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

-- Data awal
INSERT INTO layanan (nama_layanan, harga_per_kg, estimasi_hari) VALUES
 ('Cuci Kering',   5000, 2),
 ('Cuci Setrika',  7000, 3),
 ('Setrika Saja',  4000, 1);
```

---

## 4. Rancangan Class

### 4.1 Struktur Package

```
laundry
├── Main.java
├── model
│   ├── Pelanggan.java
│   ├── Layanan.java
│   ├── Transaksi.java
│   └── StatusTransaksi.java     (enum)
├── dao
│   ├── BaseDAO.java             (interface generik)
│   ├── PelangganDAO.java
│   ├── LayananDAO.java
│   └── TransaksiDAO.java
├── util
│   └── DatabaseConnection.java
└── view
    ├── MainFrame.java
    ├── BasePanel.java           (abstract class)
    ├── PelangganPanel.java
    ├── LayananPanel.java
    └── TransaksiPanel.java
```

### 4.2 Daftar Class dan Tanggung Jawabnya

**Package `model`** (menyimpan data, menerapkan *encapsulation*: atribut `private` + getter/setter)

| Class | Atribut | Method utama |
|-------|---------|--------------|
| `Pelanggan` | idPelanggan, nama, noHp, alamat, aktif | getter/setter, `toString()` (dipakai `JComboBox`) |
| `Layanan` | idLayanan, namaLayanan, hargaPerKg, estimasiHari, aktif | getter/setter, `toString()` |
| `Transaksi` | idTransaksi, pelanggan, layanan, beratKg, hargaPerKg, totalHarga, tanggalMasuk, status | getter/setter, `hitungTotal()` |
| `StatusTransaksi` (enum) | DIPROSES, SELESAI, DIAMBIL | - |

**Package `util`**

| Class | Tanggung jawab |
|-------|----------------|
| `DatabaseConnection` | Membuat dan menyediakan `Connection` JDBC ke `db_laundry` (`getConnection()`) |

**Package `dao`** (akses database; semua query SQL ada di sini)

| Class / Interface | Method |
|-------------------|--------|
| `BaseDAO<T>` (interface) | `insert(T)`, `update(T)`, `delete(int id)`, `findById(int id)`, `findAll()` |
| `PelangganDAO implements BaseDAO<Pelanggan>` | semua method `BaseDAO` + `cari(String keyword)` |
| `LayananDAO implements BaseDAO<Layanan>` | semua method `BaseDAO` |
| `TransaksiDAO implements BaseDAO<Transaksi>` | semua method `BaseDAO` + `updateStatus(int id, StatusTransaksi s)`, `filterByStatus(StatusTransaksi s)` |

Catatan: `delete()` di `PelangganDAO` dan `LayananDAO` memeriksa dulu apakah data sudah dipakai di transaksi. Jika sudah, data dinonaktifkan (`aktif = 0`), bukan dihapus permanen.

**Package `view`** (tampilan Swing)

| Class | Tanggung jawab |
|-------|----------------|
| `MainFrame extends JFrame` | Jendela utama berisi `JTabbedPane` untuk tiga panel |
| `BasePanel extends JPanel` (abstract) | Kerangka bersama: `initComponents()`, `loadData()`, `resetForm()` dideklarasikan abstract, `showError()`/`showInfo()` dipakai bersama |
| `PelangganPanel extends BasePanel` | Form dan tabel pelanggan, memakai `PelangganDAO` |
| `LayananPanel extends BasePanel` | Form dan tabel layanan, memakai `LayananDAO` |
| `TransaksiPanel extends BasePanel` | Form order, tabel riwayat, tombol ubah status, memakai `TransaksiDAO`, `PelangganDAO`, `LayananDAO` |

### 4.3 Hubungan Antar Class

```
Main ──> MainFrame ──> PelangganPanel ──> PelangganDAO ──> Pelanggan
                  ├──> LayananPanel   ──> LayananDAO   ──> Layanan
                  └──> TransaksiPanel ──> TransaksiDAO ──> Transaksi ──> Pelanggan, Layanan, StatusTransaksi

PelangganDAO / LayananDAO / TransaksiDAO ──> DatabaseConnection
```

### 4.4 Konsep PBO yang Diterapkan

| Konsep | Penerapan |
|--------|-----------|
| Encapsulation | Atribut `private` pada class model dengan getter/setter |
| Inheritance | `MainFrame extends JFrame`, `BasePanel extends JPanel`, panel lain `extends BasePanel` |
| Abstraction | `BasePanel` (abstract class) dan `BaseDAO<T>` (interface) |
| Polymorphism | Setiap DAO mengimplementasikan `BaseDAO` dengan cara berbeda; `toString()` pada model dipakai oleh `JComboBox` |
| Association | `Transaksi` memiliki objek `Pelanggan` dan `Layanan` |
| Enum | `StatusTransaksi` |
| Exception handling | `SQLException` ditangani di DAO dan ditampilkan lewat `JOptionPane` di view |

---

## 5. Rencana Pengerjaan Berikutnya

| Minggu | Target |
|--------|--------|
| 1 (2 - 6 Nov) | Dokumen ide, alur, rancangan database, dan rancangan class (dokumen ini) |
| 2 | Database + koneksi JDBC + class model + DAO; CRUD Pelanggan dan Layanan jalan |
| 3 | Panel Transaksi: input order, hitung total, riwayat |
| 4 | Ubah status, pencarian/filter, validasi input, perapihan tampilan, pengujian |
