package laundry.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import laundry.view.komponen.GarisBulat;

/**
 * Tema tampilan aplikasi (warna, font, gaya tombol/input/tabel) sesuai file desain
 * "Desain Aplikasi Laundry Kiloan Jatinangor". Semua panel memakai method di sini
 * supaya tampilannya seragam.
 */
public final class Tema {

    // ---------- Warna ----------
    public static final Color PRIMER = hex("#2B6F77");
    public static final Color PRIMER_GELAP = hex("#22585E");
    public static final Color PRIMER_TEKAN = hex("#1B474C");
    public static final Color PRIMER_MUDA = hex("#E3F1F2");
    public static final Color LATAR = hex("#F1F5F6");
    public static final Color PUTIH = Color.WHITE;
    public static final Color GARIS = hex("#DDE3E6");
    public static final Color GARIS_INPUT = hex("#8F999F");
    public static final Color TEKS = hex("#1E2A31");
    public static final Color TEKS_SEKUNDER = hex("#4A565D");
    public static final Color TEKS_REDUP = hex("#6B7780");
    public static final Color MERAH = hex("#A8321E");
    public static final Color MERAH_MUDA = hex("#FBEDEA");
    public static final Color HIJAU = hex("#2E7D4F");
    public static final Color HEADER_TABEL = hex("#EDF1F3");
    public static final Color KOTAK_ABU = hex("#ECEFF1");
    public static final Color NONAKTIF = hex("#D5D9DD");

    public static final int TINGGI_INPUT = 40;

    private static String keluargaFont = Font.SANS_SERIF;

    private Tema() {
    }

    /** Dipanggil sekali di Main sebelum membuat jendela. */
    public static void pasang() {
        FlatLaf.setGlobalExtraDefaults(Map.of("@accentColor", "#2B6F77"));
        FlatLightLaf.setup();

        // Pilih font yang tersedia (desain memakai font sans-serif modern seperti Inter)
        List<String> tersedia = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String calon : new String[]{"Inter", "Segoe UI", "SF Pro Text", "Helvetica Neue", "Arial"}) {
            if (tersedia.contains(calon)) {
                keluargaFont = calon;
                break;
            }
        }
        UIManager.put("defaultFont", font(Font.PLAIN, 14));

        // Sudut membulat & warna fokus
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 10);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("ScrollPane.arc", 10);
        UIManager.put("Component.focusWidth", 3);
        UIManager.put("Component.innerFocusWidth", 0);
        UIManager.put("Component.focusColor", new Color(43, 111, 119, 70));
        UIManager.put("Component.focusedBorderColor", PRIMER);
        UIManager.put("Component.borderColor", GARIS_INPUT);
        UIManager.put("Component.hoverBorderColor", TEKS);
        UIManager.put("Component.error.borderColor", MERAH);
        UIManager.put("Component.error.focusedBorderColor", MERAH);
        UIManager.put("Component.disabledBorderColor", GARIS);
        UIManager.put("TextField.margin", new Insets(6, 12, 6, 12));
        UIManager.put("TextArea.margin", new Insets(8, 12, 8, 12));
        UIManager.put("ComboBox.padding", new Insets(6, 12, 6, 8));
        UIManager.put("TextField.placeholderForeground", TEKS_REDUP);
        UIManager.put("TextComponent.placeholderForeground", TEKS_REDUP);
        UIManager.put("TextField.disabledBackground", KOTAK_ABU);
        UIManager.put("ComboBox.buttonStyle", "none");
        UIManager.put("ComboBox.buttonArrowColor", TEKS_SEKUNDER);
        UIManager.put("Panel.background", LATAR);
        UIManager.put("OptionPane.background", PUTIH);
        UIManager.put("OptionPane.messageFont", font(Font.PLAIN, 14));

        // Tab bergaris bawah (Pelanggan / Layanan / Transaksi)
        UIManager.put("TabbedPane.font", font(Font.PLAIN, 16));
        UIManager.put("TabbedPane.background", PUTIH);
        UIManager.put("TabbedPane.foreground", TEKS_SEKUNDER);
        UIManager.put("TabbedPane.selectedForeground", PRIMER);
        UIManager.put("TabbedPane.underlineColor", PRIMER);
        UIManager.put("TabbedPane.tabSelectionHeight", 3);
        UIManager.put("TabbedPane.tabHeight", 52);
        UIManager.put("TabbedPane.tabInsets", new Insets(0, 22, 0, 22));
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 14, 0, 0));
        UIManager.put("TabbedPane.hoverColor", hex("#F4F7F8"));
        UIManager.put("TabbedPane.focusColor", PUTIH);
        UIManager.put("TabbedPane.selectedBackground", PUTIH);
        UIManager.put("TabbedPane.contentAreaColor", GARIS);
        UIManager.put("TabbedPane.showTabSeparators", false);

        // Tabel
        UIManager.put("Table.rowHeight", 44);
        UIManager.put("Table.font", font(Font.PLAIN, 14));
        UIManager.put("Table.foreground", TEKS_SEKUNDER);
        UIManager.put("Table.background", PUTIH);
        UIManager.put("Table.selectionBackground", PRIMER_MUDA);
        UIManager.put("Table.selectionForeground", TEKS);
        UIManager.put("Table.selectionInactiveBackground", PRIMER_MUDA);
        UIManager.put("Table.selectionInactiveForeground", TEKS);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.gridColor", GARIS);
        UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
        UIManager.put("Table.cellMargins", new Insets(0, 14, 0, 14));
        UIManager.put("Table.cellFocusColor", new Color(0, 0, 0, 0));
        UIManager.put("Table.showCellFocusIndicator", false);
        UIManager.put("TableHeader.background", HEADER_TABEL);
        UIManager.put("TableHeader.foreground", TEKS_SEKUNDER);
        UIManager.put("TableHeader.font", font(Font.BOLD, 13));
        UIManager.put("TableHeader.height", 46);
        UIManager.put("TableHeader.cellMargins", new Insets(0, 14, 0, 14));
        UIManager.put("TableHeader.separatorColor", HEADER_TABEL);
        UIManager.put("TableHeader.bottomSeparatorColor", GARIS);
    }

    public static Font font(int style, float ukuran) {
        return new Font(keluargaFont, style, 12).deriveFont(style, ukuran);
    }

    public static Color hex(String kode) {
        return Color.decode(kode);
    }

    // ---------- Tombol ----------

    private static JButton tombolDasar(String teks) {
        JButton b = new JButton(teks);
        b.setFont(font(Font.BOLD, 15));
        b.setMargin(new Insets(8, 22, 8, 22));
        b.setFocusPainted(false);
        b.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        Dimension d = b.getPreferredSize();
        b.setPreferredSize(new Dimension(d.width, TINGGI_INPUT));
        return b;
    }

    /** Tombol utama (hijau toska), contoh: Simpan. */
    public static JButton tombolPrimer(String teks) {
        JButton b = tombolDasar(teks);
        gayaPrimer(b);
        return b;
    }

    public static void gayaPrimer(JButton b) {
        b.putClientProperty(FlatClientProperties.STYLE,
                "background:#2B6F77; foreground:#FFFFFF; hoverBackground:#22585E; pressedBackground:#1B474C;"
                + "borderWidth:0; focusedBorderColor:#2B6F77; disabledBackground:#D5D9DD; disabledText:#6B7780");
    }

    /** Tombol bergaris abu-abu, contoh: Ubah, Tandai Selesai. */
    public static JButton tombolSekunder(String teks) {
        JButton b = tombolDasar(teks);
        gayaSekunder(b);
        return b;
    }

    public static void gayaSekunder(JButton b) {
        b.putClientProperty(FlatClientProperties.STYLE,
                "background:#FFFFFF; foreground:#1E2A31; borderColor:#8F999F; hoverBorderColor:#1E2A31;"
                + "hoverBackground:#F4F7F8; pressedBackground:#E9EEF0; focusedBorderColor:#2B6F77");
    }

    /** Tombol bergaris merah untuk aksi berbahaya, contoh: Hapus, Batalkan. */
    public static JButton tombolBahaya(String teks) {
        JButton b = tombolDasar(teks);
        b.putClientProperty(FlatClientProperties.STYLE,
                "background:#FFFFFF; foreground:#A8321E; borderColor:#A8321E; hoverBorderColor:#8A2715;"
                + "hoverBackground:#FBEDEA; pressedBackground:#F6DCD6; focusedBorderColor:#A8321E");
        return b;
    }

    /** Tombol tanpa garis, contoh: Reset. */
    public static JButton tombolTeks(String teks) {
        JButton b = tombolDasar(teks);
        b.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        b.putClientProperty(FlatClientProperties.STYLE, "foreground:#4A565D; hoverBackground:#E9EEF0");
        return b;
    }

    // ---------- Input ----------

    /** Kotak isian dengan teks contoh (placeholder) abu-abu. */
    public static JTextField input(String placeholder) {
        JTextField f = new JTextField();
        f.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        f.setFont(font(Font.PLAIN, 15));
        f.setPreferredSize(new Dimension(120, TINGGI_INPUT));
        return f;
    }

    public static <E> JComboBox<E> comboBox(String placeholder) {
        JComboBox<E> c = new JComboBox<>();
        c.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        c.setFont(font(Font.PLAIN, 15));
        c.setPreferredSize(new Dimension(120, TINGGI_INPUT));
        c.setBackground(PUTIH);
        // Tampilkan teks contoh abu-abu saat belum ada yang dipilih
        javax.swing.ListCellRenderer<? super E> asli = c.getRenderer();
        c.setRenderer((list, value, index, isSelected, hasFocus) -> {
            Component comp = asli.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
            if (value == null && index == -1 && comp instanceof JLabel) {
                ((JLabel) comp).setText(placeholder);
                comp.setForeground(TEKS_REDUP);
            }
            return comp;
        });
        return c;
    }

    /** Judul di dalam kartu, contoh: "Daftar pelanggan". */
    public static JLabel judulKartu(String teks) {
        JLabel l = new JLabel(teks);
        l.setFont(font(Font.BOLD, 18));
        l.setForeground(TEKS);
        return l;
    }

    // ---------- Tabel ----------

    /** Membuat tabel (sel tidak bisa diedit langsung) dengan kolom yang diberikan. */
    public static JTable tabel(String... kolom) {
        DefaultTableModel model = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable t = new JTable(model);
        t.setFillsViewportHeight(true);
        t.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setResizingAllowed(true);
        return t;
    }

    /**
     * Mengatur lebar kolom dan kolom mana yang rata kanan (angka/uang).
     * Header kolom rata kanan ikut rata kanan seperti di desain.
     */
    public static void aturKolom(JTable t, int[] lebar, int... kolomRataKanan) {
        Set<Integer> kanan = new HashSet<>();
        for (int k : kolomRataKanan) {
            kanan.add(k);
        }
        TableColumnModel cm = t.getColumnModel();
        DefaultTableCellRenderer rendererKanan = new DefaultTableCellRenderer();
        rendererKanan.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int i = 0; i < cm.getColumnCount(); i++) {
            cm.getColumn(i).setPreferredWidth(lebar[i]);
            if (kanan.contains(i)) {
                cm.getColumn(i).setCellRenderer(rendererKanan);
            }
        }
        TableCellRenderer headerAsli = t.getTableHeader().getDefaultRenderer();
        t.getTableHeader().setDefaultRenderer((tbl, value, sel, focus, row, col) -> {
            Component c = headerAsli.getTableCellRendererComponent(tbl, value, sel, focus, row, col);
            if (c instanceof JLabel) {
                ((JLabel) c).setHorizontalAlignment(kanan.contains(col) ? SwingConstants.RIGHT : SwingConstants.LEFT);
            }
            return c;
        });
    }

    /** Membungkus tabel dalam kotak bergaris tipis dengan sudut membulat. */
    public static JScrollPane bungkusTabel(JTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(new GarisBulat(GARIS, 10));
        sp.getViewport().setBackground(PUTIH);
        sp.setBackground(PUTIH);
        return sp;
    }

    // ---------- Ikon aplikasi (kotak toska + tetes air) ----------

    public static Image ikonAplikasi(int ukuran) {
        BufferedImage img = new BufferedImage(ukuran, ukuran, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float s = ukuran;
        g.setColor(PRIMER);
        g.fill(new RoundRectangle2D.Float(0, 0, s, s, s * 0.45f, s * 0.45f));
        GeneralPath tetes = new GeneralPath();
        tetes.moveTo(s * 0.5f, s * 0.24f);
        tetes.curveTo(s * 0.62f, s * 0.40f, s * 0.68f, s * 0.50f, s * 0.68f, s * 0.58f);
        tetes.curveTo(s * 0.68f, s * 0.69f, s * 0.60f, s * 0.77f, s * 0.5f, s * 0.77f);
        tetes.curveTo(s * 0.40f, s * 0.77f, s * 0.32f, s * 0.69f, s * 0.32f, s * 0.58f);
        tetes.curveTo(s * 0.32f, s * 0.50f, s * 0.38f, s * 0.40f, s * 0.5f, s * 0.24f);
        tetes.closePath();
        g.setColor(PUTIH);
        g.fill(tetes);
        g.setStroke(new BasicStroke(1f));
        g.dispose();
        return img;
    }

    /** Garis tepi kosong (padding) untuk panel. */
    public static javax.swing.border.Border padding(int atas, int kiri, int bawah, int kanan) {
        return BorderFactory.createEmptyBorder(atas, kiri, bawah, kanan);
    }
}
