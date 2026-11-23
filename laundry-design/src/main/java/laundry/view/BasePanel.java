package laundry.view;

import com.formdev.flatlaf.FlatClientProperties;
import java.sql.SQLException;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Kelas induk (abstract) untuk PelangganPanel, LayananPanel, dan TransaksiPanel.
 * Berisi method yang wajib dibuat setiap panel dan method bantu untuk menampilkan pesan.
 */
public abstract class BasePanel extends JPanel {

    /** Mengisi ulang tabel dari database. */
    protected abstract void loadData();

    /** Mengosongkan form input. */
    protected abstract void resetForm();

    /** Memberi garis merah pada input dan menampilkan pesan error di bawahnya. */
    protected void tandaiError(JComponent input, JLabel lblError, String pesan) {
        input.putClientProperty(FlatClientProperties.STYLE, "borderColor:#A8321E; focusedBorderColor:#A8321E");
        lblError.setText(pesan);
    }

    /** Menghapus tanda error. */
    protected void hapusError(JComponent input, JLabel lblError) {
        input.putClientProperty(FlatClientProperties.STYLE, null);
        lblError.setText("");
    }

    /** Menampilkan pesan sukses hijau selama 3 detik. */
    protected void tampilkanSukses(JLabel lblSukses, String pesan) {
        lblSukses.setText("✓ " + pesan);
        Timer timer = new Timer(3000, e -> lblSukses.setText(""));
        timer.setRepeats(false);
        timer.start();
    }

    protected void showInfo(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showPeringatan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Perhatian", JOptionPane.WARNING_MESSAGE);
    }

    protected void showErrorDatabase(String aksi, SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gagal " + aksi + ".\nPesan database: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected boolean konfirmasi(String pesan) {
        return JOptionPane.showConfirmDialog(this, pesan, "Konfirmasi", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
    }
}
