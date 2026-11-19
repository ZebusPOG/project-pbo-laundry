package laundry.view.komponen;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import laundry.view.Tema;

/** Kotak putih bersudut bulat dengan garis tipis (wadah form dan tabel di desain). */
public class Kartu extends JPanel {

    public Kartu(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RoundRectangle2D bentuk = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, 14, 14);
        g2.setColor(Tema.PUTIH);
        g2.fill(bentuk);
        g2.setColor(Tema.GARIS);
        g2.draw(bentuk);
        g2.dispose();
        super.paintComponent(g);
    }
}
