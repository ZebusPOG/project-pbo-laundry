# Aplikasi Laundry Kiloan Jatinangor

Aplikasi desktop Java Swing + MySQL untuk mencatat pelanggan, layanan, dan transaksi laundry.

## Cara menjalankan

1. **Nyalakan MySQL** (XAMPP, MAMP, DBngin, atau Homebrew).
2. **Buat database** dengan menjalankan script [`database/db_laundry.sql`](database/db_laundry.sql):
   - Terminal: `mysql -u root -p < database/db_laundry.sql`
   - atau buka file tersebut di phpMyAdmin / MySQL Workbench lalu jalankan.
3. **Cek pengaturan koneksi** di [`laundry-design/src/main/resources/database.properties`](laundry-design/src/main/resources/database.properties)
   (default: `localhost:3306`, user `root`, password kosong).
4. **Jalankan aplikasi**:
   - VS Code: buka folder `project-pbo-laundry`, buka `Main.java`, klik **Run** (atau tekan F5).
   - Terminal: `cd laundry-design && mvn compile exec:java`
   - File .jar: `cd laundry-design && mvn package && java -jar target/laundry-design-1.0-SNAPSHOT.jar`

## Struktur kode

```
laundry-design/src/main/java/laundry
├── Main.java              titik masuk, cek koneksi database
├── model/                 Pelanggan, Layanan, Transaksi, StatusTransaksi
├── dao/                   BaseDAO + PelangganDAO, LayananDAO, TransaksiDAO (query JDBC)
├── util/                  DatabaseConnection, Rupiah, Format
└── view/                  MainFrame, BasePanel, PelangganPanel, LayananPanel, TransaksiPanel, NotaDialog
    ├── Tema.java          warna, font, gaya tombol/input/tabel sesuai file desain
    └── komponen/          Kartu, FormField, KotakNilai, StatusRenderer, AreaTeks, Ikon, GarisBulat
```

Library (diunduh otomatis oleh Maven): `mysql-connector-j` (driver MySQL) dan `flatlaf` (tampilan modern Swing).
