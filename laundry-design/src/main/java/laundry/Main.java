package laundry;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import laundry.util.DatabaseConnection;
import laundry.view.MainFrame;
import laundry.view.Tema;

/**
 * Titik masuk aplikasi Laundry Kiloan Jatinangor.
 */
public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            Tema.pasang();

            // Cek koneksi database sebelum membuka jendela utama
            try (Connection c = DatabaseConnection.getConnection()) {
                new MainFrame().setVisible(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,
                        "Tidak bisa terhubung ke database MySQL.\n\n"
                        + "Pastikan:\n"
                        + "1. MySQL sudah menyala (XAMPP / MAMP / Homebrew).\n"
                        + "2. Database db_laundry sudah dibuat (jalankan file database/db_laundry.sql).\n"
                        + "3. User dan password di src/main/resources/database.properties sudah benar.\n\n"
                        + "Alamat: " + DatabaseConnection.getUrl() + "\n"
                        + "Pesan: " + ex.getMessage(),
                        "Koneksi Database Gagal", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
