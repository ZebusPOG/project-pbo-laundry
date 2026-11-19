package laundry.view.komponen;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import laundry.view.Tema;

/** JTextArea yang menampilkan teks contoh abu-abu (placeholder) saat masih kosong. */
public class AreaTeks extends JTextArea {

    private final String placeholder;

    public AreaTeks(String placeholder, int baris, int kolom) {
        super(baris, kolom);
        this.placeholder = placeholder;
        // Samakan font dengan JTextField agar tidak memakai font Monospaced bawaan
        setFont(UIManager.getFont("TextField.font"));
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getText().isEmpty() && placeholder != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB
            );
            g2.setFont(getFont());
            g2.setColor(Tema.TEKS_REDUP);
            Insets in = getInsets();
            g2.drawString(placeholder, in.left, in.top + g2.getFontMetrics().getAscent());
            g2.dispose();
        }
    }
}
