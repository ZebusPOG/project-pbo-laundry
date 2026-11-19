package laundry.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Menyediakan koneksi JDBC ke database db_laundry.
 * Alamat, user, dan password dibaca dari file src/main/resources/database.properties.
 */
public final class DatabaseConnection {

    private static final Properties CONFIG = muatConfig();

    private DatabaseConnection() {
    }

    private static Properties muatConfig() {
        Properties p = new Properties();
        // Nilai bawaan jika file properties tidak ditemukan
        p.setProperty("db.url", "jdbc:mysql://localhost:3306/db_laundry?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta");
        p.setProperty("db.user", "root");
        p.setProperty("db.password", "");
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/database.properties")) {
            if (in != null) {
                p.load(in);
            }
        } catch (IOException ex) {
            System.err.println("Gagal membaca database.properties: " + ex.getMessage());
        }
        return p;
    }

    /** Membuka koneksi baru. Tutup kembali dengan try-with-resources setelah dipakai. */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                CONFIG.getProperty("db.url"),
                CONFIG.getProperty("db.user"),
                CONFIG.getProperty("db.password"));
    }

    /** Alamat database yang dipakai (untuk ditampilkan di pesan error). */
    public static String getUrl() {
        return CONFIG.getProperty("db.url");
    }
}
