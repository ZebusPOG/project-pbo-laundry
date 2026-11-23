package laundry.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Koneksi JDBC ke database MySQL db_laundry.
 * Database dan tabel dibuat otomatis saat aplikasi pertama kali dijalankan,
 * jadi cukup pastikan MySQL sudah menyala.
 */
public class DatabaseConnection {

    // Sesuaikan dengan MySQL di komputer masing-masing
    // (XAMPP: user root, password kosong | MAMP: password "root", port 8889)
    // createDatabaseIfNotExist=true -> database db_laundry dibuat otomatis jika belum ada
    private static final String URL = "jdbc:mysql://localhost:3306/db_laundry"
            + "?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /** Membuka koneksi baru. Tutup kembali dengan try-with-resources setelah dipakai. */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Membuat tabel jika belum ada, lalu mengisi data awal layanan. Dipanggil sekali di Main. */
    public static void siapkanDatabase() throws SQLException {
        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS pelanggan ("
                    + " id_pelanggan INT AUTO_INCREMENT PRIMARY KEY,"
                    + " nama VARCHAR(100) NOT NULL,"
                    + " no_hp VARCHAR(20),"
                    + " alamat VARCHAR(255),"
                    + " aktif TINYINT(1) NOT NULL DEFAULT 1)");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS layanan ("
                    + " id_layanan INT AUTO_INCREMENT PRIMARY KEY,"
                    + " nama_layanan VARCHAR(100) NOT NULL,"
                    + " harga_per_kg DECIMAL(10,2) NOT NULL,"
                    + " estimasi_hari INT NOT NULL DEFAULT 2,"
                    + " aktif TINYINT(1) NOT NULL DEFAULT 1)");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS transaksi ("
                    + " id_transaksi INT AUTO_INCREMENT PRIMARY KEY,"
                    + " id_pelanggan INT NOT NULL,"
                    + " id_layanan INT NOT NULL,"
                    + " berat_kg DECIMAL(5,2) NOT NULL,"
                    + " harga_per_kg DECIMAL(10,2) NOT NULL,"
                    + " total_harga DECIMAL(12,2) NOT NULL,"
                    + " tanggal_masuk DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + " status ENUM('DIPROSES','SELESAI','DIAMBIL') NOT NULL DEFAULT 'DIPROSES',"
                    + " FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id_pelanggan),"
                    + " FOREIGN KEY (id_layanan) REFERENCES layanan(id_layanan))");

            // Data awal layanan, hanya jika tabel layanan masih kosong
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM layanan")) {
                rs.next();
                if (rs.getInt(1) == 0) {
                    st.executeUpdate("INSERT INTO layanan (nama_layanan, harga_per_kg, estimasi_hari) VALUES"
                            + " ('Cuci Kering', 5000, 2),"
                            + " ('Cuci Setrika', 7000, 3),"
                            + " ('Setrika Saja', 4000, 1)");
                }
            }
        }
    }
}
