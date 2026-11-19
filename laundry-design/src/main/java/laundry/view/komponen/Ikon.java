package laundry.view.komponen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import javax.swing.Icon;
import laundry.view.Tema;

/** Ikon kecil yang digambar langsung dengan Java2D (tidak perlu file gambar). */
public final class Ikon implements Icon {

    private enum Jenis { CARI, ERROR, SUKSES }

    public static final Icon CARI = new Ikon(Jenis.CARI, Tema.TEKS_SEKUNDER, 18);
    public static final Icon ERROR = new Ikon(Jenis.ERROR, Tema.MERAH, 16);
    public static final Icon SUKSES = new Ikon(Jenis.SUKSES, Tema.HIJAU, 16);

    private final Jenis jenis;
    private final Color warna;
    private final int ukuran;

    private Ikon(Jenis jenis, Color warna, int ukuran) {
        this.jenis = jenis;
        this.warna = warna;
        this.ukuran = ukuran;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.translate(x, y);
        g2.setColor(warna);
        float s = ukuran;
        g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        switch (jenis) {
            case CARI:
                g2.draw(new Ellipse2D.Float(s * 0.15f, s * 0.15f, s * 0.55f, s * 0.55f));
                g2.draw(new Line2D.Float(s * 0.62f, s * 0.62f, s * 0.86f, s * 0.86f));
                break;
            case ERROR:
                g2.draw(new Ellipse2D.Float(1, 1, s - 2, s - 2));
                g2.draw(new Line2D.Float(s / 2, s * 0.28f, s / 2, s * 0.56f));
                g2.fill(new Ellipse2D.Float(s / 2 - 1.1f, s * 0.68f, 2.2f, 2.2f));
                break;
            default: // SUKSES
                Path2D centang = new Path2D.Float();
                centang.moveTo(s * 0.15f, s * 0.52f);
                centang.lineTo(s * 0.40f, s * 0.76f);
                centang.lineTo(s * 0.86f, s * 0.26f);
                g2.draw(centang);
                break;
        }
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return ukuran;
    }

    @Override
    public int getIconHeight() {
        return ukuran;
    }
}
