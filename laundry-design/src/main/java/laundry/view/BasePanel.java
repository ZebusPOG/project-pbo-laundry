package laundry.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import laundry.view.komponen.Ikon;

/**
 * Kerangka bersama untuk panel Pelanggan, Layanan, dan Transaksi:
 * judul + subjudul halaman, kartu form di atas, kartu tabel di bawah,
 * serta method bantu untuk menampilkan pesan.
 */
public abstract class BasePanel extends JPanel {

    private final JLabel lblSukses = new JLabel();
    private Timer timerSukses;

    protected BasePanel(String judul, String subjudul) {
        super(new BorderLayout(0, 24));
        setBackground(Tema.LATAR);
        setBorder(BorderFactory.createEmptyBorder(26, 28, 28, 28));

        JLabel lblJudul = new JLabel(judul);
        lblJudul.setFont(Tema.font(Font.BOLD, 28));
        lblJudul.setForeground(Tema.TEKS);
        JLabel lblSub = new JLabel(subjudul);
        lblSub.setFont(Tema.font(Font.PLAIN, 15));
        lblSub.setForeground(Tema.TEKS_SEKUNDER);
        lblSub.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(lblJudul);
        header.add(lblSub);
        add(header, BorderLayout.NORTH);

        lblSukses.setFont(Tema.font(Font.PLAIN, 14));
        lblSukses.setForeground(Tema.HIJAU);
        lblSukses.setIcon(Ikon.SUKSES);
        lblSukses.setIconTextGap(6);
        lblSukses.setVisible(false);
    }

    /** Membuat dan menyusun komponen panel. */
    protected abstract void initComponents();

    /** Mengisi ulang tabel dari database. */
    protected abstract void loadData();

    /** Mengosongkan form input. */
    protected abstract void resetForm();

    /** Menyusun kartu form (atas) dan kartu tabel (bawah, ikut membesar). */
    protected void setIsi(JComponent kartuAtas, JComponent kartuBawah) {
        JPanel isi = new JPanel(new BorderLayout(0, 22));
        isi.setOpaque(false);
        isi.add(kartuAtas, BorderLayout.NORTH);
        isi.add(kartuBawah, BorderLayout.CENTER);
        add(isi, BorderLayout.CENTER);
    }

    /** Label "✓ Data tersimpan" untuk diletakkan di baris tombol. */
    protected JLabel labelSukses() {
        return lblSukses;
    }

    /** Menampilkan pesan sukses hijau selama 3 detik. */
    protected void tampilkanSukses(String pesan) {
        lblSukses.setText(pesan);
        lblSukses.setVisible(true);
        if (timerSukses != null) {
            timerSukses.stop();
        }
        timerSukses = new Timer(3000, e -> lblSukses.setVisible(false));
        timerSukses.setRepeats(false);
        timerSukses.start();
    }

    protected void showInfo(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showPeringatan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Perhatian", JOptionPane.WARNING_MESSAGE);
    }

    protected void showError(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Pesan error database yang seragam. */
    protected void errorDatabase(String aksi, SQLException ex) {
        showError("Gagal " + aksi + ".\nPesan database: " + ex.getMessage());
    }

    protected boolean konfirmasi(String judul, String pesan) {
        return JOptionPane.showConfirmDialog(this, pesan, judul, JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
