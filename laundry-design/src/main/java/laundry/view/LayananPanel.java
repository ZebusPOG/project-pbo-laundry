package laundry.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import laundry.dao.LayananDAO;
import laundry.dao.TransaksiDAO;
import laundry.model.Layanan;
import laundry.util.Format;
import laundry.util.Rupiah;
import laundry.view.komponen.FormField;
import laundry.view.komponen.Kartu;

/** Tab Layanan: atur jenis layanan, harga per kg, dan estimasi hari. */
public class LayananPanel extends BasePanel {

    private final LayananDAO layananDAO = new LayananDAO();
    private final TransaksiDAO transaksiDAO = new TransaksiDAO();

    private JTextField fieldNamaLayanan;
    private JTextField fieldHargaPerKg;
    private JTextField fieldEstimasiHari;
    private FormField fNama;
    private FormField fHarga;
    private FormField fEstimasi;
    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnReset;
    private JButton btnHapus;
    private JTable tblLayanan;

    /** ID layanan yang sedang dipilih di tabel (-1 = belum ada). */
    private int idTerpilih = -1;

    public LayananPanel() {
        super("Data Layanan", "Atur jenis layanan, harga, dan estimasi");
        initComponents();
        loadData();
    }

    @Override
    protected void initComponents() {
        fieldNamaLayanan = Tema.input("Nama layanan");
        fieldHargaPerKg = Tema.input("Rp 0");
        fieldEstimasiHari = Tema.input("Jumlah hari");
        fNama = new FormField("Nama layanan", true, fieldNamaLayanan);
        fHarga = new FormField("Harga per kg (Rp)", true, fieldHargaPerKg);
        fEstimasi = new FormField("Estimasi (hari)", false, fieldEstimasiHari);

        btnSimpan = Tema.tombolPrimer("Simpan");
        btnUbah = Tema.tombolSekunder("Ubah");
        btnReset = Tema.tombolTeks("Reset");
        btnHapus = Tema.tombolBahaya("Hapus");

        JPanel barisTombol = new JPanel();
        barisTombol.setOpaque(false);
        barisTombol.setLayout(new BoxLayout(barisTombol, BoxLayout.X_AXIS));
        barisTombol.add(btnSimpan);
        barisTombol.add(Box.createHorizontalStrut(12));
        barisTombol.add(btnUbah);
        barisTombol.add(Box.createHorizontalStrut(12));
        barisTombol.add(btnReset);
        barisTombol.add(Box.createHorizontalStrut(16));
        barisTombol.add(labelSukses());
        barisTombol.add(Box.createHorizontalGlue());
        barisTombol.add(btnHapus);

        Kartu kartuForm = new Kartu(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTH;
        g.gridy = 0;
        g.gridx = 0;
        g.weightx = 0.5;
        g.insets = new Insets(0, 0, 0, 14);
        kartuForm.add(fNama, g);
        g.gridx = 1;
        g.weightx = 0.25;
        g.insets = new Insets(0, 14, 0, 14);
        kartuForm.add(fHarga, g);
        g.gridx = 2;
        g.insets = new Insets(0, 14, 0, 0);
        kartuForm.add(fEstimasi, g);
        g.gridx = 0;
        g.gridy = 1;
        g.gridwidth = 3;
        g.weightx = 1;
        g.insets = new Insets(18, 0, 0, 0);
        kartuForm.add(barisTombol, g);

        tblLayanan = Tema.tabel("ID", "Nama Layanan", "Harga per Kg", "Estimasi (hari)");
        Tema.aturKolom(tblLayanan, new int[]{60, 420, 180, 180}, 2, 3);

        Kartu kartuTabel = new Kartu(new BorderLayout(0, 18));
        kartuTabel.add(Tema.judulKartu("Daftar layanan"), BorderLayout.NORTH);
        kartuTabel.add(Tema.bungkusTabel(tblLayanan), BorderLayout.CENTER);

        setIsi(kartuForm, kartuTabel);

        btnSimpan.addActionListener(evt -> simpan());
        btnUbah.addActionListener(evt -> ubah());
        btnReset.addActionListener(evt -> resetForm());
        btnHapus.addActionListener(evt -> hapus());
        tblLayanan.getSelectionModel().addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting()) {
                pilihBaris();
            }
        });
    }

    @Override
    protected void loadData() {
        DefaultTableModel model = (DefaultTableModel) tblLayanan.getModel();
        model.setRowCount(0);
        try {
            for (Layanan l : layananDAO.findAll()) {
                model.addRow(new Object[]{l.getIdLayanan(), l.getNamaLayanan(),
                    Rupiah.format(l.getHargaPerKg()), l.getEstimasiHari()});
            }
        } catch (SQLException ex) {
            errorDatabase("memuat data layanan", ex);
        }
    }

    /**
     * Membaca dan memeriksa isi form. Mengembalikan objek Layanan berisi data form,
     * atau null (isian yang salah diberi tanda merah) jika ada input yang salah.
     */
    private Layanan bacaForm() {
        boolean valid = true;
        String nama = fieldNamaLayanan.getText().trim();
        String hargaTeks = fieldHargaPerKg.getText().trim();
        String estimasiTeks = fieldEstimasiHari.getText().trim();

        int estimasi = 2; // default sesuai rancangan database
        if (!estimasiTeks.isEmpty()) {
            try {
                estimasi = Integer.parseInt(estimasiTeks);
                if (estimasi < 1) {
                    fEstimasi.error("Minimal 1 hari");
                    valid = false;
                }
            } catch (NumberFormatException ex) {
                fEstimasi.error("Harus angka bulat, contoh: 2");
                valid = false;
            }
        }
        double harga = 0;
        if (hargaTeks.isEmpty()) {
            fHarga.error("Harga wajib diisi");
            valid = false;
        } else {
            try {
                harga = Format.parseRupiah(hargaTeks);
                if (harga <= 0) {
                    fHarga.error("Harga harus lebih dari 0");
                    valid = false;
                }
            } catch (NumberFormatException ex) {
                fHarga.error("Harus angka, contoh: 8000");
                valid = false;
            }
        }
        if (nama.isEmpty()) {
            fNama.error("Nama layanan wajib diisi");
            valid = false;
        }
        return valid ? new Layanan(nama, harga, estimasi) : null;
    }

    private void simpan() {
        Layanan baru = bacaForm();
        if (baru == null) {
            return;
        }
        try {
            layananDAO.insert(baru);
            loadData();
            resetForm();
            tampilkanSukses("Data tersimpan");
        } catch (SQLException ex) {
            errorDatabase("menyimpan layanan", ex);
        }
    }

    private void pilihBaris() {
        int baris = tblLayanan.getSelectedRow();
        if (baris < 0) {
            return;
        }
        int id = (Integer) tblLayanan.getModel().getValueAt(tblLayanan.convertRowIndexToModel(baris), 0);
        try {
            Layanan l = layananDAO.findById(id);
            if (l != null) {
                idTerpilih = id;
                fieldNamaLayanan.setText(l.getNamaLayanan());
                fieldHargaPerKg.setText(String.valueOf(Math.round(l.getHargaPerKg())));
                fieldEstimasiHari.setText(String.valueOf(l.getEstimasiHari()));
            }
        } catch (SQLException ex) {
            errorDatabase("memuat layanan", ex);
        }
    }

    /** Harga baru hanya berlaku untuk transaksi berikutnya (transaksi lama menyimpan salinan harganya). */
    private void ubah() {
        if (idTerpilih < 0) {
            showPeringatan("Klik dulu data layanan di tabel yang ingin diubah.");
            return;
        }
        Layanan isiForm = bacaForm();
        if (isiForm == null) {
            return;
        }
        try {
            isiForm.setIdLayanan(idTerpilih);
            layananDAO.update(isiForm);
            loadData();
            resetForm();
            tampilkanSukses("Perubahan tersimpan");
        } catch (SQLException ex) {
            errorDatabase("mengubah layanan", ex);
        }
    }

    private void hapus() {
        if (idTerpilih < 0) {
            showPeringatan("Klik dulu data layanan di tabel yang ingin dihapus.");
            return;
        }
        try {
            Layanan l = layananDAO.findById(idTerpilih);
            if (l == null || !konfirmasi("Konfirmasi Hapus", "Hapus layanan \"" + l.getNamaLayanan() + "\"?")) {
                return;
            }
            boolean dipakai = transaksiDAO.adaLayanan(l.getIdLayanan());
            layananDAO.delete(l.getIdLayanan());
            loadData();
            resetForm();
            if (dipakai) {
                showInfo("Layanan sudah dipakai di transaksi, jadi dinonaktifkan.\nRiwayat transaksinya tetap tersimpan.");
            } else {
                tampilkanSukses("Data dihapus");
            }
        } catch (SQLException ex) {
            errorDatabase("menghapus layanan", ex);
        }
    }

    @Override
    protected void resetForm() {
        fieldNamaLayanan.setText("");
        fieldHargaPerKg.setText("");
        fieldEstimasiHari.setText("");
        fNama.bersihkan();
        fHarga.bersihkan();
        fEstimasi.bersihkan();
        idTerpilih = -1;
        tblLayanan.clearSelection();
        fieldNamaLayanan.requestFocusInWindow();
    }
}
