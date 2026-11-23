package laundry.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Format tampilan (Rupiah, tanggal, berat) dan pembacaan angka dari input. */
public class Format {

    private static final Locale INDONESIA = Locale.forLanguageTag("id-ID");

    /** 24500 -> "Rp 24.500" */
    public static String rupiah(double nilai) {
        return "Rp " + new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(INDONESIA)).format(nilai);
    }

    /** 3.5 -> "3,5" dan 4.0 -> "4" */
    public static String berat(double kg) {
        return new DecimalFormat("#,##0.##", DecimalFormatSymbols.getInstance(INDONESIA)).format(kg);
    }

    /** Contoh: 18/11/2026 */
    public static String tanggal(LocalDateTime waktu) {
        return waktu.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /** Contoh: 18-11-2026 09:15 */
    public static String tanggalJam(LocalDateTime waktu) {
        return waktu.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    /** "8000", "8.000", atau "Rp 8.000" -> 8000 */
    public static double bacaRupiah(String teks) {
        return Double.parseDouble(teks.replaceAll("[^0-9,]", "").replace(",", "."));
    }

    /** "3,5" atau "3.5" -> 3.5 */
    public static double bacaDesimal(String teks) {
        return Double.parseDouble(teks.trim().replace(",", "."));
    }
}
