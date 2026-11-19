package laundry.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Format tampilan tanggal dan berat, serta pembacaan angka dari input pengguna. */
public final class Format {

    private static final DateTimeFormatter TANGGAL = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat BERAT =
            new DecimalFormat("#,##0.##", DecimalFormatSymbols.getInstance(Locale.forLanguageTag("id-ID")));

    private Format() {
    }

    /** Contoh: 18/11/2026 */
    public static String tanggal(LocalDateTime waktu) {
        return waktu == null ? "-" : waktu.format(TANGGAL);
    }

    /** Contoh: 3.5 -> "3,5", 4.0 -> "4" */
    public static String berat(double kg) {
        return BERAT.format(kg);
    }

    /** Membaca nominal Rupiah dari input: "8000", "8.000", atau "Rp 8.000" -> 8000. */
    public static double parseRupiah(String teks) {
        String bersih = teks.trim().replaceAll("(?i)rp", "").replace(" ", "").replace(".", "").replace(",", ".");
        return Double.parseDouble(bersih);
    }

    /** Membaca angka desimal dari input; koma boleh dipakai: "3,5" -> 3.5. */
    public static double parseDesimal(String teks) {
        return Double.parseDouble(teks.trim().replace(",", "."));
    }
}
