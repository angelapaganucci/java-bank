package br.com.dio;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Utils {

    public static String formatCurrency(BigDecimal value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return nf.format(value);
    }
}
