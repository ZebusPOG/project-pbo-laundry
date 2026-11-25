package laundry.view;

import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import laundry.dao.BaseDAO;
import laundry.dao.LayananDAO;
import laundry.dao.PelangganDAO;
import laundry.dao.TransaksiDAO;
import laundry.model.Layanan;
import laundry.model.Pelanggan;
import laundry.model.StatusTransaksi;
import laundry.model.Transaksi;
import laundry.util.Format;

/**
 * Tab Transaksi: catat cucian masuk, hitung total otomatis, dan pantau statusnya.
 * Susunan tampilan dibuat dengan GUI Builder NetBeans (klik tab "Design").
 */
public class TransaksiPanel extends BasePanel {

    // Polimorfisme: tipe referensi interface BaseDAO, objek aslinya PelangganDAO / LayananDAO
    private final BaseDAO<Pelanggan> pelangganDAO = new PelangganDAO();
    private final BaseDAO<Layanan> layananDAO = new LayananDAO();
    private final TransaksiDAO transaksiDAO = new TransaksiDAO();

    public TransaksiPanel() {
        initComponents();
        aturTampilan();
        // Jangan akses database saat panel ditampilkan di tab Design NetBeans
        // (Design view tidak memuat driver MySQL, sehingga muncul error "No suitable driver").
        if (!java.beans.Beans.isDesignTime()) {
            isiPilihan();
            loadData();
        }
    }

    /** Pengaturan yang tidak bisa dibuat di tab Design (teks contoh, garis tombol, judul tabel).
     *  Warna, font, dan ukuran diatur di tab Design -> jendela Properties. */
    private void aturTampilan() {
        Tema.combo(cmbPelanggan, "Pilih pelanggan");
        Tema.combo(cmbLayanan, "Pilih layanan");
        Tema.placeholder(txtBerat, "0");
        Tema.tombolPrimer(btnSimpan);
        Tema.tombolSekunder(btnSelesai, btnDiambil);
        Tema.tombolBahaya(btnBatal);
        Tema.tabel(tblTransaksi, 3, 4, 5);
        Tema.lebarKolom(tblTransaksi, 50, 160, 170, 80, 105, 115, 115, 125);
        tblTransaksi.getColumnModel().getColumn(7).setCellRenderer(new StatusRenderer());
    }

    /** Mengisi pilihan pelanggan dan layanan dari database. */
    private void isiPilihan() {
        try {
            cmbPelanggan.removeAllItems();
            for (Pelanggan p : pelangganDAO.findAll()) {
                cmbPelanggan.addItem(p);
            }
            cmbPelanggan.setSelectedIndex(-1);

            cmbLayanan.removeAllItems();
            for (Layanan l : layananDAO.findAll()) {
                cmbLayanan.addItem(l);
            }
            cmbLayanan.setSelectedIndex(-1);
        } catch (SQLException ex) {
            showErrorDatabase("memuat pilihan pelanggan/layanan", ex);
        }
        hitungTotal();
    }

    /** Membaca berat dari input (koma boleh, contoh 3,5); -1 jika kosong/salah. */
    private double beratInput() {
        try {
            return Format.bacaDesimal(txtBerat.getText());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /** Menampilkan harga per kg dan total harga (berat x harga) secara otomatis. */
    private void hitungTotal() {
        Layanan l = (Layanan) cmbLayanan.getSelectedItem();
        double harga = (l == null) ? 0 : l.getHargaPerKg();
        double berat = beratInput();
        lblHargaPerKg.setText(Format.rupiah(harga));
        lblTotalHarga.setText(Format.rupiah(berat > 0 ? berat * harga : 0));
    }

    @Override
    protected void loadData() {
        DefaultTableModel model = (DefaultTableModel) tblTransaksi.getModel();
        model.setRowCount(0);
        try {
            String filter = (String) cmbFilterStatus.getSelectedItem();
            List<Transaksi> data = filter.equals("Semua")
                    ? transaksiDAO.findAll()
                    : transaksiDAO.filterByStatus(StatusTransaksi.valueOf(filter));
            for (Transaksi t : data) {
                model.addRow(new Object[]{
                    t.getIdTransaksi(),
                    t.getPelanggan().getNama(),
                    t.getLayanan().getNamaLayanan(),
                    Format.berat(t.getBeratKg()),
                    Format.rupiah(t.getHargaPerKg()),
                    Format.rupiah(t.getTotalHarga()),
                    Format.tanggal(t.getTanggalMasuk()),
                    t.getStatus()
                });
            }
        } catch (SQLException ex) {
            showErrorDatabase("memuat data transaksi", ex);
        }
    }

    @Override
    protected void resetForm() {
        cmbPelanggan.setSelectedIndex(-1);
        cmbLayanan.setSelectedIndex(-1);
        txtBerat.setText("");
        hapusError(cmbPelanggan, lblErrorPelanggan);
        hapusError(cmbLayanan, lblErrorLayanan);
        hapusError(txtBerat, lblErrorBerat);
        hitungTotal();
    }

    /** Mengambil transaksi dari baris tabel yang dipilih (null jika belum memilih). */
    private Transaksi transaksiTerpilih() throws SQLException {
        int baris = tblTransaksi.getSelectedRow();
        if (baris < 0) {
            showPeringatan("Klik dulu transaksi di tabel.");
            return null;
        }
        return transaksiDAO.findById((int) tblTransaksi.getValueAt(baris, 0));
    }

    /** Mengubah status; urutannya harus DIPROSES -> SELESAI -> DIAMBIL. */
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
            showErrorDatabase("mengubah status", ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelHeader = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        lblSubjudul = new javax.swing.JLabel();
        panelIsi = new javax.swing.JPanel();
        panelForm = new javax.swing.JPanel();
        panelBaris1 = new javax.swing.JPanel();
        lblPelanggan = new javax.swing.JLabel();
        lblLayanan = new javax.swing.JLabel();
        cmbPelanggan = new javax.swing.JComboBox<>();
        cmbLayanan = new javax.swing.JComboBox<>();
        lblErrorPelanggan = new javax.swing.JLabel();
        lblErrorLayanan = new javax.swing.JLabel();
        panelBaris2 = new javax.swing.JPanel();
        lblHargaKg = new javax.swing.JLabel();
        lblBerat = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblHargaPerKg = new javax.swing.JLabel();
        txtBerat = new javax.swing.JTextField();
        lblTotalHarga = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        lblErrorBerat = new javax.swing.JLabel();
        lblSukses = new javax.swing.JLabel();
        panelTabel = new javax.swing.JPanel();
        panelAtasTabel = new javax.swing.JPanel();
        lblFilter = new javax.swing.JLabel();
        cmbFilterStatus = new javax.swing.JComboBox<>();
        btnSelesai = new javax.swing.JButton();
        btnDiambil = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        scrollTabel = new javax.swing.JScrollPane();
        tblTransaksi = new javax.swing.JTable();

        setBackground(new java.awt.Color(241, 245, 246));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(26, 28, 28, 28));
        setPreferredSize(new java.awt.Dimension(1000, 760));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        panelHeader.setOpaque(false);
        panelHeader.setLayout(new java.awt.BorderLayout());

        lblJudul.setText("Transaksi Laundry");
        lblJudul.setFont(lblJudul.getFont().deriveFont(lblJudul.getFont().getStyle() | java.awt.Font.BOLD, lblJudul.getFont().getSize()+14));
        lblJudul.setForeground(new java.awt.Color(30, 42, 49));
        panelHeader.add(lblJudul, java.awt.BorderLayout.NORTH);

        lblSubjudul.setText("Catat cucian masuk dan pantau statusnya");
        lblSubjudul.setFont(lblSubjudul.getFont().deriveFont(lblSubjudul.getFont().getSize()+1f));
        lblSubjudul.setForeground(new java.awt.Color(74, 86, 93));
        panelHeader.add(lblSubjudul, java.awt.BorderLayout.CENTER);

        add(panelHeader, java.awt.BorderLayout.NORTH);

        panelIsi.setOpaque(false);
        panelIsi.setLayout(new java.awt.BorderLayout());

        panelForm.setBackground(new java.awt.Color(255, 255, 255));
        panelForm.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(221, 227, 230), 1, true), javax.swing.BorderFactory.createEmptyBorder(24, 24, 24, 24)));
        panelForm.setLayout(new java.awt.GridBagLayout());

        panelBaris1.setOpaque(false);
        panelBaris1.setLayout(new java.awt.GridBagLayout());

        lblPelanggan.setText("<html>Pelanggan <font color='#A8321E'>*</font></html>");
        lblPelanggan.setFont(lblPelanggan.getFont().deriveFont(lblPelanggan.getFont().getStyle() | java.awt.Font.BOLD));
        lblPelanggan.setForeground(new java.awt.Color(74, 86, 93));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 14);
        panelBaris1.add(lblPelanggan, gridBagConstraints);

        lblLayanan.setText("<html>Layanan <font color='#A8321E'>*</font></html>");
        lblLayanan.setFont(lblLayanan.getFont().deriveFont(lblLayanan.getFont().getStyle() | java.awt.Font.BOLD));
        lblLayanan.setForeground(new java.awt.Color(74, 86, 93));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 8, 0);
        panelBaris1.add(lblLayanan, gridBagConstraints);

        cmbPelanggan.setPreferredSize(new java.awt.Dimension(120, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 14);
        panelBaris1.add(cmbPelanggan, gridBagConstraints);

        cmbLayanan.setPreferredSize(new java.awt.Dimension(120, 40));
        cmbLayanan.addActionListener(this::cmbLayananActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        panelBaris1.add(cmbLayanan, gridBagConstraints);

        lblErrorPelanggan.setFont(lblErrorPelanggan.getFont().deriveFont(lblErrorPelanggan.getFont().getSize()-1f));
        lblErrorPelanggan.setForeground(new java.awt.Color(168, 50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 14);
        panelBaris1.add(lblErrorPelanggan, gridBagConstraints);

        lblErrorLayanan.setFont(lblErrorLayanan.getFont().deriveFont(lblErrorLayanan.getFont().getSize()-1f));
        lblErrorLayanan.setForeground(new java.awt.Color(168, 50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 14, 0, 0);
        panelBaris1.add(lblErrorLayanan, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panelForm.add(panelBaris1, gridBagConstraints);

        panelBaris2.setOpaque(false);
        panelBaris2.setLayout(new java.awt.GridBagLayout());

        lblHargaKg.setText("Harga per kg");
        lblHargaKg.setFont(lblHargaKg.getFont().deriveFont(lblHargaKg.getFont().getStyle() | java.awt.Font.BOLD));
        lblHargaKg.setForeground(new java.awt.Color(74, 86, 93));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 14);
        panelBaris2.add(lblHargaKg, gridBagConstraints);

        lblBerat.setText("<html>Berat (kg) <font color='#A8321E'>*</font></html>");
        lblBerat.setFont(lblBerat.getFont().deriveFont(lblBerat.getFont().getStyle() | java.awt.Font.BOLD));
        lblBerat.setForeground(new java.awt.Color(74, 86, 93));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 8, 14);
        panelBaris2.add(lblBerat, gridBagConstraints);

        lblTotal.setText("Total harga");
        lblTotal.setFont(lblTotal.getFont().deriveFont(lblTotal.getFont().getStyle() | java.awt.Font.BOLD));
        lblTotal.setForeground(new java.awt.Color(74, 86, 93));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 8, 14);
        panelBaris2.add(lblTotal, gridBagConstraints);

        lblHargaPerKg.setText("Rp 0");
        lblHargaPerKg.setOpaque(true);
        lblHargaPerKg.setBackground(new java.awt.Color(236, 239, 241));
        lblHargaPerKg.setForeground(new java.awt.Color(30, 42, 49));
        lblHargaPerKg.setFont(lblHargaPerKg.getFont().deriveFont(lblHargaPerKg.getFont().getSize()+1f));
        lblHargaPerKg.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 14, 0, 14));
        lblHargaPerKg.setPreferredSize(new java.awt.Dimension(120, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 14);
        panelBaris2.add(lblHargaPerKg, gridBagConstraints);

        txtBerat.setPreferredSize(new java.awt.Dimension(120, 40));
        txtBerat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBeratKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 14);
        panelBaris2.add(txtBerat, gridBagConstraints);

        lblTotalHarga.setBackground(new java.awt.Color(227, 241, 242));
        lblTotalHarga.setFont(lblTotalHarga.getFont().deriveFont(lblTotalHarga.getFont().getStyle() | java.awt.Font.BOLD, lblTotalHarga.getFont().getSize()+10));
        lblTotalHarga.setForeground(new java.awt.Color(30, 42, 49));
        lblTotalHarga.setText("Rp 0");
        lblTotalHarga.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 14, 0, 14));
        lblTotalHarga.setOpaque(true);
        lblTotalHarga.setPreferredSize(new java.awt.Dimension(120, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 14);
        panelBaris2.add(lblTotalHarga, gridBagConstraints);

        btnSimpan.setText("Simpan");
        btnSimpan.setBackground(new java.awt.Color(43, 111, 119));
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setFont(btnSimpan.getFont().deriveFont(btnSimpan.getFont().getStyle() | java.awt.Font.BOLD, btnSimpan.getFont().getSize()+1));
        btnSimpan.setPreferredSize(new java.awt.Dimension(150, 40));
        btnSimpan.addActionListener(this::btnSimpanActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        panelBaris2.add(btnSimpan, gridBagConstraints);

        lblErrorBerat.setFont(lblErrorBerat.getFont().deriveFont(lblErrorBerat.getFont().getSize()-1f));
        lblErrorBerat.setForeground(new java.awt.Color(168, 50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 14, 0, 14);
        panelBaris2.add(lblErrorBerat, gridBagConstraints);

        lblSukses.setForeground(new java.awt.Color(46, 125, 79));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 14, 0, 0);
        panelBaris2.add(lblSukses, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        panelForm.add(panelBaris2, gridBagConstraints);

        panelIsi.add(panelForm, java.awt.BorderLayout.NORTH);

        panelTabel.setBackground(new java.awt.Color(255, 255, 255));
        panelTabel.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(221, 227, 230), 1, true), javax.swing.BorderFactory.createEmptyBorder(24, 24, 24, 24)));
        panelTabel.setLayout(new java.awt.BorderLayout());

        panelAtasTabel.setOpaque(false);
        panelAtasTabel.setLayout(new java.awt.GridBagLayout());

        lblFilter.setText("Filter status");
        lblFilter.setFont(lblFilter.getFont().deriveFont(lblFilter.getFont().getStyle() | java.awt.Font.BOLD));
        lblFilter.setForeground(new java.awt.Color(74, 86, 93));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelAtasTabel.add(lblFilter, gridBagConstraints);

        cmbFilterStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua", "DIPROSES", "SELESAI", "DIAMBIL" }));
        cmbFilterStatus.setPreferredSize(new java.awt.Dimension(170, 40));
        cmbFilterStatus.addActionListener(this::cmbFilterStatusActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        panelAtasTabel.add(cmbFilterStatus, gridBagConstraints);

        btnSelesai.setText("Tandai Selesai");
        btnSelesai.setForeground(new java.awt.Color(30, 42, 49));
        btnSelesai.setFont(btnSelesai.getFont().deriveFont(btnSelesai.getFont().getStyle() | java.awt.Font.BOLD, btnSelesai.getFont().getSize()+1));
        btnSelesai.setPreferredSize(new java.awt.Dimension(164, 40));
        btnSelesai.addActionListener(this::btnSelesaiActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        panelAtasTabel.add(btnSelesai, gridBagConstraints);

        btnDiambil.setText("Tandai Diambil");
        btnDiambil.setForeground(new java.awt.Color(30, 42, 49));
        btnDiambil.setFont(btnDiambil.getFont().deriveFont(btnDiambil.getFont().getStyle() | java.awt.Font.BOLD, btnDiambil.getFont().getSize()+1));
        btnDiambil.setPreferredSize(new java.awt.Dimension(166, 40));
        btnDiambil.addActionListener(this::btnDiambilActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelAtasTabel.add(btnDiambil, gridBagConstraints);

        btnBatal.setText("Batalkan");
        btnBatal.setForeground(new java.awt.Color(168, 50, 30));
        btnBatal.setFont(btnBatal.getFont().deriveFont(btnBatal.getFont().getStyle() | java.awt.Font.BOLD, btnBatal.getFont().getSize()+1));
        btnBatal.setPreferredSize(new java.awt.Dimension(123, 40));
        btnBatal.addActionListener(this::btnBatalActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelAtasTabel.add(btnBatal, gridBagConstraints);

        panelTabel.add(panelAtasTabel, java.awt.BorderLayout.NORTH);

        scrollTabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(221, 227, 230), 1, true));

        tblTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Pelanggan", "Layanan", "<html><div style='text-align:right'>Berat<br>(kg)</div></html>", "Harga/kg", "Total", "<html>Tanggal<br>Masuk</html>", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTransaksi.setFillsViewportHeight(true);
        tblTransaksi.setGridColor(new java.awt.Color(221, 227, 230));
        tblTransaksi.setIntercellSpacing(new java.awt.Dimension(0, 1));
        tblTransaksi.setRowHeight(44);
        tblTransaksi.setSelectionBackground(new java.awt.Color(227, 241, 242));
        tblTransaksi.setSelectionForeground(new java.awt.Color(30, 42, 49));
        tblTransaksi.setShowHorizontalLines(true);
        tblTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransaksiMouseClicked(evt);
            }
        });
        scrollTabel.setViewportView(tblTransaksi);

        panelTabel.add(scrollTabel, java.awt.BorderLayout.CENTER);

        panelIsi.add(panelTabel, java.awt.BorderLayout.CENTER);

        add(panelIsi, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // setiap tab Transaksi dibuka, muat ulang pilihan (agar pelanggan/layanan baru langsung muncul)
        isiPilihan();
        loadData();
    }//GEN-LAST:event_formComponentShown

    private void cmbLayananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLayananActionPerformed
        hitungTotal();
    }//GEN-LAST:event_cmbLayananActionPerformed

    private void txtBeratKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBeratKeyReleased
        hitungTotal();
    }//GEN-LAST:event_txtBeratKeyReleased

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        Pelanggan p = (Pelanggan) cmbPelanggan.getSelectedItem();
        Layanan l = (Layanan) cmbLayanan.getSelectedItem();
        double berat = beratInput();

        hapusError(cmbPelanggan, lblErrorPelanggan);
        hapusError(cmbLayanan, lblErrorLayanan);
        hapusError(txtBerat, lblErrorBerat);
        boolean valid = true;
        if (p == null) {
            tandaiError(cmbPelanggan, lblErrorPelanggan, "Pilih pelanggan");
            valid = false;
        }
        if (l == null) {
            tandaiError(cmbLayanan, lblErrorLayanan, "Pilih layanan");
            valid = false;
        }
        if (berat <= 0) {
            tandaiError(txtBerat, lblErrorBerat, "Berat harus angka lebih dari 0");
            valid = false;
        }
        if (!valid) {
            return;
        }
        try {
            transaksiDAO.insert(new Transaksi(p, l, berat)); // status awal DIPROSES
            resetForm();
            loadData();
            tampilkanSukses(lblSukses, "Transaksi tersimpan");
        } catch (SQLException ex) {
            showErrorDatabase("menyimpan transaksi", ex);
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void cmbFilterStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFilterStatusActionPerformed
        loadData();
    }//GEN-LAST:event_cmbFilterStatusActionPerformed

    private void btnSelesaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelesaiActionPerformed
        ubahStatus(StatusTransaksi.SELESAI);
    }//GEN-LAST:event_btnSelesaiActionPerformed

    private void btnDiambilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiambilActionPerformed
        ubahStatus(StatusTransaksi.DIAMBIL);
    }//GEN-LAST:event_btnDiambilActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        try {
            Transaksi t = transaksiTerpilih();
            if (t == null) {
                return;
            }
            if (t.getStatus() == StatusTransaksi.DIAMBIL) {
                showPeringatan("Cucian sudah diambil pelanggan, transaksi tidak bisa dibatalkan.");
                return;
            }
            if (konfirmasi("Batalkan transaksi " + t.getIdTransaksi() + " milik " + t.getPelanggan().getNama()
                    + " (" + Format.rupiah(t.getTotalHarga()) + ")?")) {
                transaksiDAO.delete(t.getIdTransaksi());
                loadData();
            }
        } catch (SQLException ex) {
            showErrorDatabase("membatalkan transaksi", ex);
        }
    }//GEN-LAST:event_btnBatalActionPerformed

    private void tblTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransaksiMouseClicked
        // baris yang diklik dipakai oleh tombol Tandai Selesai / Tandai Diambil / Batalkan
    }//GEN-LAST:event_tblTransaksiMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnDiambil;
    private javax.swing.JButton btnSelesai;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbFilterStatus;
    private javax.swing.JComboBox<laundry.model.Layanan> cmbLayanan;
    private javax.swing.JComboBox<laundry.model.Pelanggan> cmbPelanggan;
    private javax.swing.JLabel lblBerat;
    private javax.swing.JLabel lblErrorBerat;
    private javax.swing.JLabel lblErrorLayanan;
    private javax.swing.JLabel lblErrorPelanggan;
    private javax.swing.JLabel lblFilter;
    private javax.swing.JLabel lblHargaKg;
    private javax.swing.JLabel lblHargaPerKg;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblLayanan;
    private javax.swing.JLabel lblPelanggan;
    private javax.swing.JLabel lblSubjudul;
    private javax.swing.JLabel lblSukses;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalHarga;
    private javax.swing.JPanel panelAtasTabel;
    private javax.swing.JPanel panelBaris1;
    private javax.swing.JPanel panelBaris2;
    private javax.swing.JPanel panelForm;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelIsi;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JScrollPane scrollTabel;
    private javax.swing.JTable tblTransaksi;
    private javax.swing.JTextField txtBerat;
    // End of variables declaration//GEN-END:variables
}
