package laundry.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import laundry.model.StatusTransaksi;

/** Menampilkan status transaksi di tabel sebagai label berwarna (DIPROSES kuning, SELESAI hijau, DIAMBIL abu). */
public class StatusRenderer extends DefaultTableCellRenderer {

    private Color warnaLatar;
    private Color warnaTeks;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
        if (value == StatusTransaksi.DIPROSES) {
            warnaLatar = new Color(0xFCF1C7);
            warnaTeks = new Color(0x7A5A00);
        } else if (value == StatusTransaksi.SELESAI) {
            warnaLatar = new Color(0xDDF3E4);
            warnaTeks = new Color(0x1F6B3A);
        } else {
            warnaLatar = new Color(0xE4E8EB);
            warnaTeks = new Color(0x3F4A51);
        }
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
        int lebar = g2.getFontMetrics().stringWidth(getText()) + 24;
        int y = (getHeight() - 26) / 2;
        g2.setColor(warnaLatar);
        g2.fillRoundRect(14, y, lebar, 26, 26, 26);
        g2.setColor(warnaTeks);
        g2.drawString(getText(), 26, y + 18);
    }
}
