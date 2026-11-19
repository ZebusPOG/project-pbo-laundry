package laundry.util;

import java.text.NumberFormat;
import java.util.Locale;

/** Format angka ke Rupiah, contoh: 24500 -> "Rp 24.500". */
public final class Rupiah {

    private static final NumberFormat FORMAT = NumberFormat.getIntegerInstance(Locale.forLanguageTag("id-ID"));

    private Rupiah() {
    }

    public static String format(double nilai) {
        return "Rp " + FORMAT.format(Math.round(nilai));
    }
}
