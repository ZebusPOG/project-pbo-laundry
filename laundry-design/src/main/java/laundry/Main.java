package laundry;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import laundry.util.DatabaseConnection;
import laundry.view.MainFrame;
import laundry.view.Tema;

/** Titik masuk aplikasi Laundry Kiloan Jatinangor. */
public class Main {

    public static void main(String[] args) {
        Tema.pasang();
        java.awt.EventQueue.invokeLater(() -> {
            // Hubungkan ke MySQL, buat database & tabel jika belum ada, lalu buka jendela utama
            try {
                DatabaseConnection.siapkanDatabase();
                new MainFrame().setVisible(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,
                        "Tidak bisa terhubung ke database MySQL.\n\n"
                        + "1. Pastikan MySQL sudah menyala (XAMPP / MAMP / Homebrew).\n"
                        + "2. Cek user & password di laundry/util/DatabaseConnection.java.\n\n"
                        + "Pesan: " + ex.getMessage(),
                        "Koneksi Database Gagal", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
