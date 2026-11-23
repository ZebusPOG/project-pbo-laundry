# Aplikasi Laundry Kiloan Jatinangor

Aplikasi desktop Java Swing + MySQL untuk mencatat pelanggan, layanan, dan transaksi laundry.

## Cara menjalankan di NetBeans

1. **Nyalakan MySQL** (XAMPP, MAMP, atau Homebrew).
2. Database **tidak perlu dibuat manual**: saat aplikasi pertama kali dijalankan, database `db_laundry`,
   ketiga tabelnya, dan 3 layanan awal dibuat otomatis (lihat `DatabaseConnection.siapkanDatabase()`).
   File [`database/db_laundry.sql`](database/db_laundry.sql) tetap disediakan jika ingin membuatnya lewat phpMyAdmin.
3. **Cek user & password MySQL** di [`laundry/util/DatabaseConnection.java`](laundry-design/src/main/java/laundry/util/DatabaseConnection.java)
   (default: `localhost:3306`, user `root`, password kosong).
4. Di NetBeans: **File → Open Project** → pilih folder **`laundry-design`** (yang berisi `pom.xml`).
5. Klik kanan proyek → **Clean and Build** (pertama kali, Maven mengunduh driver MySQL dan FlatLaf).
6. Tekan **Run (F6)**.

Tampilan setiap tab bisa diubah lewat GUI Builder: buka `PelangganPanel.java`, `LayananPanel.java`,
`TransaksiPanel.java`, atau `MainFrame.java`, lalu klik tab **Design**.

## Struktur kode

```
laundry-design/src/main/java/laundry
├── Main.java                 titik masuk: pasang tema, siapkan database, buka MainFrame
├── model/                    Pelanggan, Layanan, Transaksi, StatusTransaksi
├── dao/                      BaseDAO (interface) + PelangganDAO, LayananDAO, TransaksiDAO (query JDBC)
├── util/                     DatabaseConnection (koneksi MySQL + buat tabel otomatis), Format (Rupiah, tanggal, berat)
└── view/                     tampilan (JFrame/JPanel Form NetBeans)
    ├── MainFrame             jendela utama berisi 3 tab
    ├── BasePanel             kelas induk abstract untuk ketiga panel
    ├── PelangganPanel        tab Pelanggan
    ├── LayananPanel          tab Layanan
    ├── TransaksiPanel        tab Transaksi
    ├── StatusRenderer        label status berwarna di tabel transaksi
    └── Tema                  warna & gaya sesuai file desain
```
