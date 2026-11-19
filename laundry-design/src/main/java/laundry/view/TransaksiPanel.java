package laundry.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.HierarchyEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import laundry.dao.LayananDAO;
import laundry.dao.PelangganDAO;
import laundry.dao.TransaksiDAO;
import laundry.model.Layanan;
import laundry.model.Pelanggan;
import laundry.model.StatusTransaksi;
import laundry.model.Transaksi;
import laundry.util.Format;
import laundry.util.Rupiah;
import laundry.view.komponen.FormField;
import laundry.view.komponen.Kartu;
import laundry.view.komponen.KotakNilai;
import laundry.view.komponen.StatusRenderer;

/** Tab Transaksi: catat cucian masuk, hitung total otomatis, dan pantau statusnya. */
public class TransaksiPanel extends BasePanel {

    private final PelangganDAO pelangganDAO = new PelangganDAO();
    private final LayananDAO layananDAO = new LayananDAO();
    private final TransaksiDAO transaksiDAO = new TransaksiDAO();

    private JComboBox<Pelanggan> cmbPelanggan;
    private JComboBox<Layanan> cmbLayanan;
    private JTextField fieldBerat;
    private KotakNilai kotakHarga;
    private KotakNilai kotakTotal;
    private FormField fPelanggan;
    private FormField fLayanan;
    private FormField fBerat;
    private JButton btnSimpan;
    private JComboBox<String> cmbFilterStatus;
    private JButton btnCetakNota;
    private JButton btnSelesai;
    private JButton btnDiambil;
    private JButton btnBatal;
    private JTable tblTransaksi;

    public TransaksiPanel() {
        super("Transaksi Laundry", "Catat cucian masuk dan pantau statusnya");
        initComponents();
        loadData();
    }

    @Override
    protected void initComponents() {
        // ----- Kartu form order -----
        cmbPelanggan = Tema.comboBox("Pilih pelanggan");
        cmbLayanan = Tema.comboBox("Pilih layanan");
        fieldBerat = Tema.input("0");
        kotakHarga = new KotakNilai(Tema.KOTAK_ABU, Tema.font(Font.PLAIN, 15), "Rp 0");
        kotakTotal = new KotakNilai(Tema.PRIMER_MUDA, Tema.font(Font.BOLD, 24), "Rp 0");
        btnSimpan = Tema.tombolPrimer("Simpan");
        btnSimpan.setPreferredSize(new Dimension(150, Tema.TINGGI_INPUT));

        fPelanggan = new FormField("Pelanggan", true, cmbPelanggan);
        fLayanan = new FormField("Layanan", true, cmbLayanan);
        fBerat = new FormField("Berat (kg)", true, fieldBerat);
        FormField fHarga = new FormField("Harga per kg", false, kotakHarga);
        FormField fTotal = new FormField("Total harga", false, kotakTotal);

        Kartu kartuForm = new Kartu(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTH;

        // Baris 1: Pelanggan | Layanan
        JPanel baris1 = new JPanel(new GridBagLayout());
        baris1.setOpaque(false);
        GridBagConstraints b = new GridBagConstraints();
        b.fill = GridBagConstraints.HORIZONTAL;
        b.anchor = GridBagConstraints.NORTH;
        b.weightx = 0.5;
        b.insets = new Insets(0, 0, 0, 14);
        baris1.add(fPelanggan, b);
        b.gridx = 1;
        b.insets = new Insets(0, 14, 0, 0);
        baris1.add(fLayanan, b);

        // Baris 2: Harga per kg | Berat | Total harga | Simpan
        JPanel baris2 = new JPanel(new GridBagLayout());
        baris2.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 0;
        c.weightx = 0.26;
        c.insets = new Insets(0, 0, 0, 14);
        baris2.add(fHarga, c);
        c.gridx = 1;
        c.insets = new Insets(0, 14, 0, 14);
        baris2.add(fBerat, c);
        c.gridx = 2;
        c.weightx = 0.32;
        baris2.add(fTotal, c);
        c.gridx = 3;
        c.weightx = 0;
        c.insets = new Insets(0, 14, 0, 0);
        JPanel wadahSimpan = new JPanel(new BorderLayout(0, 6));
        wadahSimpan.setOpaque(false);
        JLabel jarak = new JLabel(" ");
        jarak.setFont(Tema.font(Font.BOLD, 14));
        wadahSimpan.add(jarak, BorderLayout.NORTH);
        wadahSimpan.add(btnSimpan, BorderLayout.CENTER);
        wadahSimpan.add(labelSukses(), BorderLayout.SOUTH);
        baris2.add(wadahSimpan, c);

        g.gridy = 0;
        g.weightx = 1;
        kartuForm.add(baris1, g);
        g.gridy = 1;
        g.insets = new Insets(18, 0, 0, 0);
        kartuForm.add(baris2, g);

        // ----- Kartu tabel riwayat -----
        cmbFilterStatus = Tema.comboBox("Semua");
        cmbFilterStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Semua", "DIPROSES", "SELESAI", "DIAMBIL"}));
        cmbFilterStatus.setPreferredSize(new Dimension(170, Tema.TINGGI_INPUT));
        cmbFilterStatus.setMaximumSize(new Dimension(170, Tema.TINGGI_INPUT));
        JLabel lblFilter = new JLabel("Filter status");
        lblFilter.setFont(Tema.font(Font.BOLD, 14));
        lblFilter.setForeground(Tema.TEKS_SEKUNDER);

        btnCetakNota = Tema.tombolSekunder("Cetak Nota");
        btnSelesai = Tema.tombolSekunder("Tandai Selesai");
        btnDiambil = Tema.tombolSekunder("Tandai Diambil");
        btnBatal = Tema.tombolBahaya("Batalkan");

        JPanel atasTabel = new JPanel();
        atasTabel.setOpaque(false);
        atasTabel.setLayout(new BoxLayout(atasTabel, BoxLayout.X_AXIS));
        atasTabel.add(lblFilter);
        atasTabel.add(Box.createHorizontalStrut(12));
        atasTabel.add(cmbFilterStatus);
        atasTabel.add(Box.createHorizontalGlue());
        atasTabel.add(btnCetakNota);
        atasTabel.add(Box.createHorizontalStrut(10));
        atasTabel.add(btnSelesai);
        atasTabel.add(Box.createHorizontalStrut(10));
        atasTabel.add(btnDiambil);
        atasTabel.add(Box.createHorizontalStrut(10));
        atasTabel.add(btnBatal);

        tblTransaksi = Tema.tabel("ID", "Pelanggan", "Layanan", "<html><div style='text-align:right'>Berat<br>(kg)</div></html>",
                "Harga/kg", "Total", "<html>Tanggal<br>Masuk</html>", "Status");
        Tema.aturKolom(tblTransaksi, new int[]{50, 160, 160, 90, 105, 115, 125, 120}, 3, 4, 5);
        tblTransaksi.getColumnModel().getColumn(7).setCellRenderer(new StatusRenderer());

        Kartu kartuTabel = new Kartu(new BorderLayout(0, 18));
        kartuTabel.add(atasTabel, BorderLayout.NORTH);
        kartuTabel.add(Tema.bungkusTabel(tblTransaksi), BorderLayout.CENTER);

        setIsi(kartuForm, kartuTabel);

        // ----- Event -----
        btnSimpan.addActionListener(evt -> simpan());
        cmbLayanan.addActionListener(evt -> hitungTotal());
        cmbFilterStatus.addActionListener(evt -> loadData());
        btnSelesai.addActionListener(evt -> ubahStatus(StatusTransaksi.SELESAI));
        btnDiambil.addActionListener(evt -> ubahStatus(StatusTransaksi.DIAMBIL));
        btnBatal.addActionListener(evt -> batalkan());
        btnCetakNota.addActionListener(evt -> cetakNota());
        fieldBerat.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { hitungTotal(); }
            @Override public void removeUpdate(DocumentEvent e) { hitungTotal(); }
            @Override public void changedUpdate(DocumentEvent e) { hitungTotal(); }
        });
        // Isi ulang pilihan pelanggan/layanan setiap kali tab Transaksi tampil,
        // supaya data baru dari tab Pelanggan/Layanan langsung muncul.
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                isiComboBox();
                loadData();
            }
        });
    }

    /** Mengisi combo box pelanggan dan layanan; pilihan yang sedang dipilih dipertahankan. */
    private void isiComboBox() {
        Pelanggan pDipilih = (Pelanggan) cmbPelanggan.getSelectedItem();
        Layanan lDipilih = (Layanan) cmbLayanan.getSelectedItem();
        try {
            DefaultComboBoxModel<Pelanggan> mp = new DefaultComboBoxModel<>();
            int indexP = -1;
            for (Pelanggan p : pelangganDAO.findAll()) {
                mp.addElement(p);
                if (pDipilih != null && p.getIdPelanggan() == pDipilih.getIdPelanggan()) {
                    indexP = mp.getSize() - 1;
                }
            }
            cmbPelanggan.setModel(mp);
            cmbPelanggan.setSelectedIndex(indexP);

            DefaultComboBoxModel<Layanan> ml = new DefaultComboBoxModel<>();
            int indexL = -1;
            for (Layanan l : layananDAO.findAll()) {
                ml.addElement(l);
                if (lDipilih != null && l.getIdLayanan() == lDipilih.getIdLayanan()) {
                    indexL = ml.getSize() - 1;
                }
            }
            cmbLayanan.setModel(ml);
            cmbLayanan.setSelectedIndex(indexL);
        } catch (SQLException ex) {
            errorDatabase("memuat pilihan pelanggan/layanan", ex);
        }
        hitungTotal();
    }

    /** Membaca berat dari input; -1 jika kosong/tidak valid. Koma boleh dipakai (3,5). */
    private double beratInput() {
        try {
            return Format.parseDesimal(fieldBerat.getText());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /** Menampilkan harga per kg dan total harga secara otomatis. */
    private void hitungTotal() {
        Layanan l = (Layanan) cmbLayanan.getSelectedItem();
        double harga = (l == null) ? 0 : l.getHargaPerKg();
        double berat = beratInput();
        kotakHarga.setNilai(Rupiah.format(harga));
        kotakTotal.setNilai(Rupiah.format(berat > 0 ? berat * harga : 0));
    }

    @Override
    protected void loadData() {
        DefaultTableModel model = (DefaultTableModel) tblTransaksi.getModel();
        // simpan pilihan agar tidak hilang setelah tabel dimuat ulang
        int idDipilih = idTerpilih();
        model.setRowCount(0);
        try {
            Object filter = cmbFilterStatus.getSelectedItem();
            List<Transaksi> data = (filter == null || "Semua".equals(filter))
                    ? transaksiDAO.findAll()
                    : transaksiDAO.filterByStatus(StatusTransaksi.valueOf(filter.toString()));
            for (Transaksi t : data) {
                model.addRow(new Object[]{
                    t.getIdTransaksi(),
                    t.getPelanggan().getNama(),
                    t.getLayanan().getNamaLayanan(),
                    Format.berat(t.getBeratKg()),
                    Rupiah.format(t.getHargaPerKg()),
                    Rupiah.format(t.getTotalHarga()),
                    Format.tanggal(t.getTanggalMasuk()),
                    t.getStatus()
                });
                if (t.getIdTransaksi() == idDipilih) {
                    int baris = tblTransaksi.convertRowIndexToView(model.getRowCount() - 1);
                    SwingUtilities.invokeLater(() -> tblTransaksi.setRowSelectionInterval(baris, baris));
                }
            }
        } catch (SQLException ex) {
            errorDatabase("memuat data transaksi", ex);
        }
    }

    /** Tombol Simpan: validasi, lalu simpan transaksi baru berstatus DIPROSES. */
    private void simpan() {
        Pelanggan p = (Pelanggan) cmbPelanggan.getSelectedItem();
        Layanan l = (Layanan) cmbLayanan.getSelectedItem();
        double berat = beratInput();
        boolean valid = true;
        if (p == null) {
            fPelanggan.error(cmbPelanggan.getItemCount() == 0
                    ? "Belum ada pelanggan, tambahkan di tab Pelanggan" : "Pilih pelanggan");
            valid = false;
        }
        if (l == null) {
            fLayanan.error("Pilih layanan");
            valid = false;
        }
        if (berat <= 0) {
            fBerat.error(fieldBerat.getText().trim().isEmpty() ? "Berat wajib diisi" : "Berat harus angka > 0");
            valid = false;
        }
        if (!valid) {
            return;
        }
        try {
            transaksiDAO.insert(new Transaksi(p, l, berat));
            resetForm();
            loadData();
            tampilkanSukses("Transaksi tersimpan");
        } catch (SQLException ex) {
            errorDatabase("menyimpan transaksi", ex);
        }
    }

    /** ID transaksi pada baris yang dipilih, atau -1. */
    private int idTerpilih() {
        int baris = tblTransaksi.getSelectedRow();
        return baris < 0 ? -1
                : (Integer) tblTransaksi.getModel().getValueAt(tblTransaksi.convertRowIndexToModel(baris), 0);
    }

    /** Mengambil transaksi yang dipilih dari database; menampilkan peringatan jika belum memilih. */
    private Transaksi transaksiTerpilih() throws SQLException {
        int id = idTerpilih();
        if (id < 0) {
            showPeringatan("Klik dulu transaksi di tabel.");
            return null;
        }
        return transaksiDAO.findById(id);
    }

    /** Tombol Tandai Selesai / Tandai Diambil. Urutan status: DIPROSES -> SELESAI -> DIAMBIL. */
    private void ubahStatus(StatusTransaksi statusBaru) {
        try {
            Transaksi t = transaksiTerpilih();
            if (t == null) {
                return;
            }
            StatusTransaksi syarat = (statusBaru == StatusTransaksi.SELESAI)
                    ? StatusTransaksi.DIPROSES : StatusTransaksi.SELESAI;
            if (t.getStatus() != syarat) {
                showPeringatan("Transaksi ini berstatus " + t.getStatus() + ".\n"
                        + "Hanya transaksi berstatus " + syarat + " yang bisa ditandai " + statusBaru + ".");
                return;
            }
            transaksiDAO.updateStatus(t.getIdTransaksi(), statusBaru);
            loadData();
        } catch (SQLException ex) {
            errorDatabase("mengubah status transaksi", ex);
        }
    }

    /** Tombol Batalkan: hapus transaksi yang salah input (yang sudah diambil tidak bisa dibatalkan). */
    private void batalkan() {
        try {
            Transaksi t = transaksiTerpilih();
            if (t == null) {
                return;
            }
            if (t.getStatus() == StatusTransaksi.DIAMBIL) {
                showPeringatan("Cucian sudah diambil pelanggan, transaksi tidak bisa dibatalkan.");
                return;
            }
            if (!konfirmasi("Konfirmasi Batalkan", "Batalkan transaksi " + t.getIdTransaksi() + " milik "
                    + t.getPelanggan().getNama() + " (" + Rupiah.format(t.getTotalHarga()) + ")?")) {
                return;
            }
            transaksiDAO.delete(t.getIdTransaksi());
            tblTransaksi.clearSelection();
            loadData();
        } catch (SQLException ex) {
            errorDatabase("membatalkan transaksi", ex);
        }
    }

    /** Tombol Cetak Nota: pratinjau nota transaksi terpilih, lalu bisa dicetak / disimpan PDF. */
    private void cetakNota() {
        try {
            Transaksi t = transaksiTerpilih();
            if (t == null) {
                return;
            }
            String isiNota = NotaDialog.buatIsiNota(t.getIdTransaksi(), t.getPelanggan().getNama(),
                    t.getLayanan().getNamaLayanan(), t.getBeratKg(), t.getHargaPerKg(), t.getTotalHarga(),
                    t.getTanggalMasuk(), t.getStatus());
            new NotaDialog(SwingUtilities.getWindowAncestor(this), isiNota).setVisible(true);
        } catch (SQLException ex) {
            errorDatabase("memuat transaksi", ex);
        }
    }

    @Override
    protected void resetForm() {
        cmbPelanggan.setSelectedIndex(-1);
        cmbLayanan.setSelectedIndex(-1);
        fieldBerat.setText("");
        fPelanggan.bersihkan();
        fLayanan.bersihkan();
        fBerat.bersihkan();
        hitungTotal();
    }
}
