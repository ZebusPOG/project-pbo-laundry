package laundry.view;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import laundry.dao.PelangganDAO;
import laundry.dao.TransaksiDAO;
import laundry.model.Pelanggan;
import laundry.view.komponen.AreaTeks;
import laundry.view.komponen.FormField;
import laundry.view.komponen.Ikon;
import laundry.view.komponen.Kartu;

/** Tab Pelanggan: tambah, ubah, hapus, dan cari data pelanggan. */
public class PelangganPanel extends BasePanel {

    private final PelangganDAO pelangganDAO = new PelangganDAO();
    private final TransaksiDAO transaksiDAO = new TransaksiDAO();

    private JTextField fieldNama;
    private JTextField fieldNoHp;
    private JTextArea txtAlamat;
    private JTextField fieldCari;
    private FormField fNama;
    private FormField fNoHp;
    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnReset;
    private JButton btnHapus;
    private JTable tblPelanggan;

    /** ID pelanggan yang sedang dipilih di tabel (-1 = belum ada). */
    private int idTerpilih = -1;

    public PelangganPanel() {
        super("Data Pelanggan", "Kelola data pelanggan laundry");
        initComponents();
        loadData();
    }

    @Override
    protected void initComponents() {
        // ----- Kartu form -----
        fieldNama = Tema.input("Nama pelanggan");
        fieldNoHp = Tema.input("08xx-xxxx-xxxx");
        txtAlamat = new AreaTeks("Alamat atau nama kos", 2, 20);
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        txtAlamat.setFont(Tema.font(Font.PLAIN, 15));
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);
        scrollAlamat.setPreferredSize(new Dimension(100, 84));

        fNama = new FormField("Nama", true, fieldNama);
        fNoHp = new FormField("No. HP", false, fieldNoHp);
        FormField fAlamat = new FormField("Alamat / Kos", false, txtAlamat, scrollAlamat);

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
        g.weightx = 0.5;
        g.gridx = 0;
        g.insets = new Insets(0, 0, 0, 14);
        kartuForm.add(fNama, g);
        g.gridx = 1;
        g.insets = new Insets(0, 14, 0, 0);
        kartuForm.add(fNoHp, g);
        g.gridx = 0;
        g.gridy = 1;
        g.gridwidth = 2;
        g.weightx = 1;
        g.insets = new Insets(18, 0, 0, 0);
        kartuForm.add(fAlamat, g);
        g.gridy = 2;
        g.insets = new Insets(18, 0, 0, 0);
        kartuForm.add(barisTombol, g);

        // ----- Kartu tabel -----
        fieldCari = Tema.input("Cari nama");
        fieldCari.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, Ikon.CARI);
        fieldCari.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        fieldCari.setPreferredSize(new Dimension(300, Tema.TINGGI_INPUT));

        JPanel atasTabel = new JPanel(new BorderLayout());
        atasTabel.setOpaque(false);
        atasTabel.add(Tema.judulKartu("Daftar pelanggan"), BorderLayout.WEST);
        atasTabel.add(fieldCari, BorderLayout.EAST);

        tblPelanggan = Tema.tabel("ID", "Nama", "No. HP", "Alamat");
        Tema.aturKolom(tblPelanggan, new int[]{60, 220, 200, 360});

        Kartu kartuTabel = new Kartu(new BorderLayout(0, 18));
        kartuTabel.add(atasTabel, BorderLayout.NORTH);
        kartuTabel.add(Tema.bungkusTabel(tblPelanggan), BorderLayout.CENTER);

        setIsi(kartuForm, kartuTabel);

        // ----- Event -----
        btnSimpan.addActionListener(evt -> simpan());
        btnUbah.addActionListener(evt -> ubah());
        btnReset.addActionListener(evt -> resetForm());
        btnHapus.addActionListener(evt -> hapus());
        tblPelanggan.getSelectionModel().addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting()) {
                pilihBaris();
            }
        });
        fieldCari.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { loadData(); }
            @Override public void removeUpdate(DocumentEvent e) { loadData(); }
            @Override public void changedUpdate(DocumentEvent e) { loadData(); }
        });
    }

    @Override
    protected void loadData() {
        DefaultTableModel model = (DefaultTableModel) tblPelanggan.getModel();
        model.setRowCount(0);
        try {
            String keyword = fieldCari.getText().trim();
            List<Pelanggan> data = keyword.isEmpty() ? pelangganDAO.findAll() : pelangganDAO.cari(keyword);
            for (Pelanggan p : data) {
                model.addRow(new Object[]{p.getIdPelanggan(), p.getNama(), p.getNoHp(), p.getAlamat()});
            }
        } catch (SQLException ex) {
            errorDatabase("memuat data pelanggan", ex);
        }
    }

    /** Memeriksa isi form; menandai isian yang salah dan mengembalikan false jika ada. */
    private boolean inputValid() {
        boolean valid = true;
        String noHp = fieldNoHp.getText().trim();
        if (!noHp.isEmpty()) {
            String digit = noHp.replaceAll("[\\s-]", "");
            if (!digit.matches("\\+?[0-9]{8,15}")) {
                fNoHp.error("No. HP hanya angka, contoh: 0812-3456-7890");
                fieldNoHp.requestFocus();
                valid = false;
            }
        }
        if (fieldNama.getText().trim().isEmpty()) {
            fNama.error("Nama wajib diisi");
            fieldNama.requestFocus();
            valid = false;
        }
        return valid;
    }

    /** Tombol Simpan: simpan sebagai pelanggan baru. */
    private void simpan() {
        if (!inputValid()) {
            return;
        }
        try {
            pelangganDAO.insert(new Pelanggan(fieldNama.getText().trim(), fieldNoHp.getText().trim(),
                    txtAlamat.getText().trim()));
            loadData();
            resetForm();
            tampilkanSukses("Data tersimpan");
        } catch (SQLException ex) {
            errorDatabase("menyimpan pelanggan", ex);
        }
    }

    /** Klik baris tabel: muat data pelanggan tersebut ke form. */
    private void pilihBaris() {
        int baris = tblPelanggan.getSelectedRow();
        if (baris < 0) {
            return;
        }
        int id = (Integer) tblPelanggan.getModel().getValueAt(tblPelanggan.convertRowIndexToModel(baris), 0);
        try {
            Pelanggan p = pelangganDAO.findById(id);
            if (p != null) {
                idTerpilih = id;
                fieldNama.setText(p.getNama());
                fieldNoHp.setText(p.getNoHp());
                txtAlamat.setText(p.getAlamat());
            }
        } catch (SQLException ex) {
            errorDatabase("memuat pelanggan", ex);
        }
    }

    /** Tombol Ubah: simpan perubahan data pelanggan yang dipilih. */
    private void ubah() {
        if (idTerpilih < 0) {
            showPeringatan("Klik dulu data pelanggan di tabel yang ingin diubah.");
            return;
        }
        if (!inputValid()) {
            return;
        }
        try {
            Pelanggan p = new Pelanggan(fieldNama.getText().trim(), fieldNoHp.getText().trim(), txtAlamat.getText().trim());
            p.setIdPelanggan(idTerpilih);
            pelangganDAO.update(p);
            loadData();
            resetForm();
            tampilkanSukses("Perubahan tersimpan");
        } catch (SQLException ex) {
            errorDatabase("mengubah pelanggan", ex);
        }
    }

    /** Tombol Hapus: konfirmasi, lalu hapus (atau nonaktifkan jika sudah punya transaksi). */
    private void hapus() {
        if (idTerpilih < 0) {
            showPeringatan("Klik dulu data pelanggan di tabel yang ingin dihapus.");
            return;
        }
        try {
            Pelanggan p = pelangganDAO.findById(idTerpilih);
            if (p == null || !konfirmasi("Konfirmasi Hapus", "Hapus pelanggan \"" + p.getNama() + "\"?")) {
                return;
            }
            boolean punyaTransaksi = transaksiDAO.adaPelanggan(p.getIdPelanggan());
            pelangganDAO.delete(p.getIdPelanggan());
            loadData();
            resetForm();
            if (punyaTransaksi) {
                showInfo("Pelanggan sudah punya transaksi, jadi dinonaktifkan.\nRiwayat transaksinya tetap tersimpan.");
            } else {
                tampilkanSukses("Data dihapus");
            }
        } catch (SQLException ex) {
            errorDatabase("menghapus pelanggan", ex);
        }
    }

    @Override
    protected void resetForm() {
        fieldNama.setText("");
        fieldNoHp.setText("");
        txtAlamat.setText("");
        fNama.bersihkan();
        fNoHp.bersihkan();
        idTerpilih = -1;
        tblPelanggan.clearSelection();
        fieldNama.requestFocusInWindow();
    }
}
