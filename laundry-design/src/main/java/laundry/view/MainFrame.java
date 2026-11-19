package laundry.view;

import java.awt.Dimension;
import java.awt.Taskbar;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

/** Jendela utama aplikasi: tab Pelanggan, Layanan, Transaksi. */
public class MainFrame extends JFrame {

    private final JTabbedPane tabUtama = new JTabbedPane();

    public MainFrame() {
        setTitle("Aplikasi Laundry Kiloan Jatinangor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImages(List.of(Tema.ikonAplikasi(16), Tema.ikonAplikasi(32), Tema.ikonAplikasi(64), Tema.ikonAplikasi(128)));
        try {
            Taskbar.getTaskbar().setIconImage(Tema.ikonAplikasi(256)); // ikon di Dock (macOS)
        } catch (UnsupportedOperationException | SecurityException ex) {
            // tidak semua sistem operasi mendukung, abaikan
        }

        tabUtama.addTab("Pelanggan", new PelangganPanel());
        tabUtama.addTab("Layanan", new LayananPanel());
        tabUtama.addTab("Transaksi", new TransaksiPanel());
        tabUtama.setOpaque(true);
        tabUtama.setBackground(Tema.PUTIH);
        setContentPane(tabUtama);

        int tinggiLayar = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
        setSize(1000, Math.min(880, tinggiLayar));
        setMinimumSize(new Dimension(900, 720));
        setLocationRelativeTo(null);
    }
}
