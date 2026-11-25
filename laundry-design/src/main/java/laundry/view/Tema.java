package laundry.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Pengaturan tampilan yang TIDAK bisa diatur lewat tab Design NetBeans.
 *
 * Warna, font, dan ukuran (lebar/tinggi) setiap komponen diatur di tab Design
 * (jendela Properties), sehingga yang terlihat di Design sama dengan saat program dijalankan.
 * Kelas ini hanya berisi:
 * - pemasangan FlatLaf dan aturan umum (sudut membulat, gaya tab),
 * - teks contoh abu-abu (placeholder) pada kotak isian,
 * - warna garis tepi tombol,
 * - judul kolom tabel dan kolom angka rata kanan.
 */
public class Tema {

    public static final Color PRIMER = new Color(0x2B6F77); // hijau toska

    /** Dipanggil sekali di Main: memasang FlatLaf dan aturan umum. */
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
        UIManager.put("TabbedPane.selectedForeground", PRIMER);
        UIManager.put("TabbedPane.underlineColor", PRIMER);
        UIManager.put("TabbedPane.tabSelectionHeight", 3);
        UIManager.put("TabbedPane.tabHeight", 52);
        UIManager.put("TabbedPane.tabInsets", new Insets(0, 22, 0, 22));
        UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
        UIManager.put("TabbedPane.focusColor", Color.WHITE);
        UIManager.put("TabbedPane.hoverColor", new Color(0xF4F7F8));
        UIManager.put("Table.cellMargins", new Insets(0, 14, 0, 14));
        UIManager.put("TableHeader.background", new Color(0xEDF1F3));
        UIManager.put("TableHeader.height", 46);
        UIManager.put("TableHeader.cellMargins", new Insets(0, 14, 0, 14));
    }

    /** Teks contoh abu-abu yang tampil saat kotak isian masih kosong. */
    public static void placeholder(JTextField field, String teks) {
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, teks);
    }

    /** Teks contoh abu-abu pada combo box saat belum ada yang dipilih. */
    public static <E> void combo(JComboBox<E> combo, String teks) {
        javax.swing.ListCellRenderer<? super E> asli = combo.getRenderer();
        combo.setRenderer((list, value, index, dipilih, fokus) -> {
            Component c = asli.getListCellRendererComponent(list, value, index, dipilih, fokus);
            if (value == null && index == -1) {
                ((JLabel) c).setText(teks);
            }
            return c;
        });
    }

    /** Tombol utama (Simpan): tanpa garis tepi. Warna latar diatur di tab Design. */
    public static void tombolPrimer(JButton... tombol) {
        gayaGaris("borderWidth:0", tombol);
    }

    /** Tombol biasa (Ubah, Tandai Selesai, Tandai Diambil): garis tepi abu-abu. */
    public static void tombolSekunder(JButton... tombol) {
        gayaGaris("borderColor:#8F999F", tombol);
    }

    /** Tombol berbahaya (Hapus, Batalkan): garis tepi merah. */
    public static void tombolBahaya(JButton... tombol) {
        gayaGaris("borderColor:#A8321E", tombol);
    }

    private static void gayaGaris(String style, JButton... tombol) {
        for (JButton b : tombol) {
            b.putClientProperty(FlatClientProperties.STYLE, style);
        }
    }

    /** Mengatur lebar awal setiap kolom tabel. */
    public static void lebarKolom(JTable tabel, int... lebar) {
        for (int i = 0; i < lebar.length; i++) {
            tabel.getColumnModel().getColumn(i).setPreferredWidth(lebar[i]);
        }
    }

    /** Judul kolom tebal, dan kolom angka/uang (beserta judulnya) rata kanan. */
    public static void tabel(JTable tabel, int... kolomRataKanan) {
        tabel.getTableHeader().setReorderingAllowed(false);
        tabel.getTableHeader().setFont(tabel.getTableHeader().getFont().deriveFont(Font.BOLD, 13f));
        DefaultTableCellRenderer kanan = new DefaultTableCellRenderer();
        kanan.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int k : kolomRataKanan) {
            tabel.getColumnModel().getColumn(k).setCellRenderer(kanan);
        }
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
