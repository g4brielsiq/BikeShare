package br.ufal.ic.p2.bikeshare.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

public final class FormatadorDataMoeda {

    private static final DecimalFormat FORMATADOR_MOEDA;
    private static final DateTimeFormatter FORMATADOR_DATA_HORA =
            DateTimeFormatter.ofPattern("d/M/uuuu H:mm").withResolverStyle(ResolverStyle.STRICT);

    static {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.forLanguageTag("pt-BR"));
        simbolos.setDecimalSeparator(',');
        FORMATADOR_MOEDA = new DecimalFormat("0.00", simbolos);
        FORMATADOR_MOEDA.setGroupingUsed(false);
        FORMATADOR_MOEDA.setRoundingMode(RoundingMode.HALF_UP);
    }

    private FormatadorDataMoeda() {
    }

    public static String formatarMoeda(BigDecimal valor) {
        if (valor == null) {
            return "0,00";
        }
        return FORMATADOR_MOEDA.format(valor.setScale(2, RoundingMode.HALF_UP));
    }

    public static String formatarDataHora(LocalDateTime dataHora) {
        return dataHora.format(FORMATADOR_DATA_HORA);
    }

    public static DateTimeFormatter getFormatadorDataHora() {
        return FORMATADOR_DATA_HORA;
    }
}
