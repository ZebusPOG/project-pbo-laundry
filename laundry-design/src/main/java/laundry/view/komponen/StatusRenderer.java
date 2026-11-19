package laundry.view.komponen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import laundry.model.StatusTransaksi;
import laundry.view.Tema;

/** Menampilkan status transaksi sebagai label berwarna (badge) di tabel. */
public class StatusRenderer extends DefaultTableCellRenderer {

    private String teks = "";
    private Color latarBadge = Tema.KOTAK_ABU;
    private Color warnaTeks = Tema.TEKS;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
        teks = value == null ? "" : value.toString();
        StatusTransaksi s = (value instanceof StatusTransaksi) ? (StatusTransaksi) value : null;
        if (s == StatusTransaksi.DIPROSES) {
            latarBadge = Tema.hex("#FCF1C7");
            warnaTeks = Tema.hex("#7A5A00");
        } else if (s == StatusTransaksi.SELESAI) {
            latarBadge = Tema.hex("#DDF3E4");
            warnaTeks = Tema.hex("#1F6B3A");
        } else {
            latarBadge = Tema.hex("#E4E8EB");
            warnaTeks = Tema.hex("#3F4A51");
        }
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setFont(Tema.font(Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        int lebar = fm.stringWidth(teks) + 24;
        int tinggi = 26;
        int x = 14;
        int y = (getHeight() - tinggi) / 2;
        g2.setColor(latarBadge);
        g2.fillRoundRect(x, y, lebar, tinggi, tinggi, tinggi);
        g2.setColor(warnaTeks);
        g2.drawString(teks, x + 12, y + (tinggi - fm.getHeight()) / 2 + fm.getAscent());
        g2.dispose();
    }
}
