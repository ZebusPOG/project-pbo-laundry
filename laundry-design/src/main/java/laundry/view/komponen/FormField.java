package laundry.view.komponen;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import laundry.view.Tema;

/**
 * Satu isian form: label di atas (bertanda * merah jika wajib), komponen input,
 * dan pesan di bawah (error merah / sukses hijau) sesuai "State Komponen" di desain.
 */
public class FormField extends JPanel {

    private final JComponent tampilan;
    private final JLabel lblPesan = new JLabel();

    public FormField(String label, boolean wajib, JComponent input) {
        this(label, wajib, input, input);
    }

    /**
     * @param input    komponen yang diisi pengguna (JTextField, JTextArea, JComboBox)
     * @param tampilan komponen yang ditampilkan (misal JScrollPane pembungkus JTextArea)
     */
    public FormField(String label, boolean wajib, JComponent input, JComponent tampilan) {
        super(new BorderLayout(0, 8));
        this.tampilan = tampilan;
        setOpaque(false);

        JLabel lblJudul = new JLabel("<html>" + label
                + (wajib ? " <span style='color:#A8321E'>*</span>" : "") + "</html>");
        lblJudul.setFont(Tema.font(Font.BOLD, 14));
        lblJudul.setForeground(Tema.TEKS_SEKUNDER);

        lblPesan.setFont(Tema.font(Font.PLAIN, 13));
        lblPesan.setIconTextGap(6);
        lblPesan.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lblPesan.setVisible(false);

        add(lblJudul, BorderLayout.NORTH);
        add(tampilan, BorderLayout.CENTER);
        add(lblPesan, BorderLayout.SOUTH);

        // Pesan error hilang otomatis saat pengguna mulai mengetik / memilih
        if (input instanceof JTextComponent) {
            ((JTextComponent) input).getDocument().addDocumentListener(new DocumentListener() {
                @Override public void insertUpdate(DocumentEvent e) { bersihkan(); }
                @Override public void removeUpdate(DocumentEvent e) { bersihkan(); }
                @Override public void changedUpdate(DocumentEvent e) { bersihkan(); }
            });
        } else if (input instanceof JComboBox) {
            ((JComboBox<?>) input).addActionListener(e -> bersihkan());
        }
    }

    /** Garis merah + pesan error, contoh: "Nama wajib diisi". */
    public void error(String pesan) {
        warnaGaris("#A8321E");
        tampilkanPesan(pesan, Tema.MERAH, Ikon.ERROR);
    }

    /** Garis hijau + pesan sukses, contoh: "Data tersimpan". */
    public void sukses(String pesan) {
        warnaGaris("#2E7D4F");
        tampilkanPesan(pesan, Tema.HIJAU, Ikon.SUKSES);
    }

    /** Mengganti warna garis tepi input (garis tipis seperti di desain). */
    private void warnaGaris(String warna) {
        tampilan.putClientProperty(FlatClientProperties.STYLE, "borderColor:" + warna
                + "; focusedBorderColor:" + warna);
    }

    /** Kembali ke tampilan normal. */
    public void bersihkan() {
        if (tampilan.getClientProperty(FlatClientProperties.STYLE) != null || lblPesan.isVisible()) {
            tampilan.putClientProperty(FlatClientProperties.STYLE, null);
            lblPesan.setVisible(false);
            revalidate();
        }
    }

    private void tampilkanPesan(String pesan, java.awt.Color warna, javax.swing.Icon ikon) {
        lblPesan.setText(pesan);
        lblPesan.setForeground(warna);
        lblPesan.setIcon(ikon);
        lblPesan.setVisible(true);
        revalidate();
    }
}
