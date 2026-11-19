package laundry.view.komponen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import laundry.view.Tema;

/** Kotak berwarna untuk menampilkan nilai yang tidak bisa diketik (Harga per kg, Total harga). */
public class KotakNilai extends JPanel {

    private final JLabel label = new JLabel();
    private final Color latar;

    public KotakNilai(Color latar, Font font, String nilaiAwal) {
        super(new BorderLayout());
        this.latar = latar;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 3 + 13, 0, 16));
        label.setFont(font);
        label.setForeground(Tema.TEKS);
        label.setText(nilaiAwal);
        add(label, BorderLayout.CENTER);
        setPreferredSize(new Dimension(120, Tema.TINGGI_INPUT));
    }

    public void setNilai(String nilai) {
        label.setText(nilai);
    }

    public String getNilai() {
        return label.getText();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(latar);
        g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 10, 10);
        g2.dispose();
        super.paintComponent(g);
    }
}
