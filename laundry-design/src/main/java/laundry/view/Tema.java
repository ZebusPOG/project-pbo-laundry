package laundry.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Warna dan gaya tampilan sesuai file desain.
 * Susunan komponen dibuat di GUI Builder (tab Design), method di sini hanya mengatur gayanya.
 */
public class Tema {

    public static final Color PRIMER = new Color(0x2B6F77);      // hijau toska
    public static final Color PRIMER_MUDA = new Color(0xE3F1F2);
    public static final Color LATAR = new Color(0xF1F5F6);
    public static final Color GARIS = new Color(0xDDE3E6);
    public static final Color TEKS = new Color(0x1E2A31);
    public static final Color TEKS_ABU = new Color(0x4A565D);
    public static final Color MERAH = new Color(0xA8321E);
    public static final Color HIJAU = new Color(0x2E7D4F);
    public static final Color ABU_MUDA = new Color(0xECEFF1);
    public static final int TINGGI = 40; // tinggi input dan tombol

    /** Dipanggil sekali di Main: memasang FlatLaf dan aturan umum (sudut bulat, warna tab, tabel). */
    public static void pasang() {
        FlatLightLaf.setup();
        UIManager.put("defaultFont", UIManager.getFont("defaultFont").deriveFont(14f));
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 10);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("Component.focusColor", new Color(43, 111, 119, 70));
        UIManager.put("Component.focusedBorderColor", PRIMER);
        UIManager.put("Component.borderColor", new Color(0x8F999F));
        UIManager.put("TextField.margin", new Insets(6, 12, 6, 12));
        UIManager.put("ComboBox.buttonStyle", "none");
        UIManager.put("TabbedPane.font", UIManager.getFont("defaultFont").deriveFont(16f));
        UIManager.put("TabbedPane.selectedForeground", PRIMER);
        UIManager.put("TabbedPane.underlineColor", PRIMER);
        UIManager.put("TabbedPane.tabSelectionHeight", 3);
        UIManager.put("TabbedPane.tabHeight", 52);
        UIManager.put("TabbedPane.tabInsets", new Insets(0, 22, 0, 22));
        UIManager.put("TabbedPane.background", Color.WHITE);
        UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
        UIManager.put("TabbedPane.focusColor", Color.WHITE);
        UIManager.put("TabbedPane.hoverColor", new Color(0xF4F7F8));
        UIManager.put("Table.rowHeight", 44);
        UIManager.put("Table.selectionBackground", PRIMER_MUDA);
        UIManager.put("Table.selectionForeground", TEKS);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.gridColor", GARIS);
        UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
        UIManager.put("Table.cellMargins", new Insets(0, 14, 0, 14));
        UIManager.put("TableHeader.background", new Color(0xEDF1F3));
        UIManager.put("TableHeader.height", 46);
        UIManager.put("TableHeader.cellMargins", new Insets(0, 14, 0, 14));
    }

    /** Judul besar dan subjudul halaman. */
    public static void judul(JLabel judul, JLabel subjudul) {
        judul.setFont(judul.getFont().deriveFont(Font.BOLD, 28f));
        judul.setForeground(TEKS);
        subjudul.setFont(subjudul.getFont().deriveFont(15f));
        subjudul.setForeground(TEKS_ABU);
    }

    /** Judul di dalam kartu, contoh "Daftar pelanggan". */
    public static void judulKartu(JLabel label) {
        label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));
        label.setForeground(TEKS);
    }

    /** Label isian (tebal, abu tua). */
    public static void label(JLabel... labels) {
        for (JLabel l : labels) {
            l.setFont(l.getFont().deriveFont(Font.BOLD));
            l.setForeground(TEKS_ABU);
        }
    }

    /** Label pesan error (merah) dan sukses (hijau); kosong = tidak terlihat. */
    public static void labelPesan(JLabel sukses, JLabel... error) {
        sukses.setForeground(HIJAU);
        for (JLabel l : error) {
            l.setForeground(MERAH);
            l.setFont(l.getFont().deriveFont(13f));
        }
    }

    /** Kotak isian dengan teks contoh abu-abu. */
    public static void input(JTextField field, String placeholder) {
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        field.setPreferredSize(new Dimension(120, TINGGI));
    }

    /** Combo box dengan teks contoh saat belum ada yang dipilih. */
    public static <E> void combo(JComboBox<E> combo, String placeholder) {
        combo.setPreferredSize(new Dimension(120, TINGGI));
        javax.swing.ListCellRenderer<? super E> asli = combo.getRenderer();
        combo.setRenderer((list, value, index, dipilih, fokus) -> {
            Component c = asli.getListCellRendererComponent(list, value, index, dipilih, fokus);
            if (value == null && index == -1) {
                ((JLabel) c).setText(placeholder);
            }
            return c;
        });
    }

    /** Kotak nilai yang tidak bisa diketik (Harga per kg, Total harga). */
    public static void kotakNilai(JLabel label, Color latar, float ukuranFont) {
        label.setOpaque(true);
        label.setBackground(latar);
        label.setForeground(TEKS);
        label.setBorder(new EmptyBorder(0, 14, 0, 14));
        label.setFont(label.getFont().deriveFont(ukuranFont >= 20 ? Font.BOLD : Font.PLAIN, ukuranFont));
        label.setPreferredSize(new Dimension(120, TINGGI));
    }

    public static void tombolPrimer(JButton... tombol) {
        gayaTombol("background:#2B6F77; foreground:#FFFFFF; hoverBackground:#22585E; borderWidth:0", tombol);
    }

    public static void tombolSekunder(JButton... tombol) {
        gayaTombol("background:#FFFFFF; foreground:#1E2A31; borderColor:#8F999F", tombol);
    }

    public static void tombolBahaya(JButton... tombol) {
        gayaTombol("background:#FFFFFF; foreground:#A8321E; borderColor:#A8321E; hoverBackground:#FBEDEA", tombol);
    }

    /** Tombol tanpa garis, contoh Reset. */
    public static void tombolTeks(JButton... tombol) {
        gayaTombol("foreground:#4A565D", tombol);
        for (JButton b : tombol) {
            b.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        }
    }

    private static void gayaTombol(String style, JButton... tombol) {
        for (JButton b : tombol) {
            b.putClientProperty(FlatClientProperties.STYLE, style);
            b.setFont(b.getFont().deriveFont(Font.BOLD, 15f));
            b.setPreferredSize(new Dimension(b.getPreferredSize().width + 28, TINGGI));
        }
    }

    /** Mengatur lebar awal setiap kolom tabel. */
    public static void lebarKolom(JTable tabel, int... lebar) {
        for (int i = 0; i < lebar.length; i++) {
            tabel.getColumnModel().getColumn(i).setPreferredWidth(lebar[i]);
        }
    }

    /** Gaya tabel: bingkai tipis, kolom tertentu rata kanan (angka/uang). */
    public static void tabel(JTable tabel, JScrollPane scroll, int... kolomRataKanan) {
        scroll.setBorder(new LineBorder(GARIS, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        tabel.getTableHeader().setReorderingAllowed(false);
        tabel.getTableHeader().setFont(tabel.getTableHeader().getFont().deriveFont(Font.BOLD, 13f));
        DefaultTableCellRenderer kanan = new DefaultTableCellRenderer();
        kanan.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int k : kolomRataKanan) {
            tabel.getColumnModel().getColumn(k).setCellRenderer(kanan);
        }
        // judul kolom angka ikut rata kanan
        TableCellRenderer headerAsli = tabel.getTableHeader().getDefaultRenderer();
        tabel.getTableHeader().setDefaultRenderer((t, value, sel, focus, row, col) -> {
            Component c = headerAsli.getTableCellRendererComponent(t, value, sel, focus, row, col);
            boolean rataKanan = false;
            for (int k : kolomRataKanan) {
                rataKanan |= (k == col);
            }
            ((JLabel) c).setHorizontalAlignment(rataKanan ? SwingConstants.RIGHT : SwingConstants.LEFT);
            return c;
        });
    }
}
