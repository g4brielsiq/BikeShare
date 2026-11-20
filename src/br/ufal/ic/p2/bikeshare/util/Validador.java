package br.ufal.ic.p2.bikeshare.util;

import br.ufal.ic.p2.bikeshare.excecoes.ParametroInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.ParametroNuloException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public final class Validador {

    private Validador() {
    }

    public static boolean isVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public static void validarNaoVazio(String valor, String mensagemErro) {
        if (isVazio(valor)) {
            throw new ParametroNuloException(mensagemErro);
        }
    }

    public static void validarEmail(String email) {
        validarNaoVazio(email, "Email nao pode ser nulo.");
        if (!email.contains("@")) {
            throw new ParametroInvalidoException("Email invalido.");
        }
    }

    public static BigDecimal converterDecimalNaoNegativo(String valorStr, String msgNulo,
                                                         String msgNaoNumerico, String msgNegativo) {
        validarNaoVazio(valorStr, msgNulo);
        String texto = valorStr.trim();
        if (!texto.matches("^-?\\d+([.,]\\d+)?$")) {
            throw new ParametroInvalidoException(msgNaoNumerico);
        }
        BigDecimal valor;
        try {
            valor = new BigDecimal(texto.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new ParametroInvalidoException(msgNaoNumerico);
        }
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new ParametroInvalidoException(msgNegativo);
        }
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal converterDecimalPositivo(String valorStr, String msgNulo,
                                                      String msgNaoNumerico, String msgNaoPositivo) {
        BigDecimal valor = converterDecimalNaoNegativo(valorStr, msgNulo, msgNaoNumerico, msgNaoPositivo);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParametroInvalidoException(msgNaoPositivo);
        }
        return valor;
    }

    public static void validarValidadeCartao(String validade) {
        validarNaoVazio(validade, "Validade do cartao nao pode ser nula.");
        if (!validade.trim().matches("^(0[1-9]|1[0-2])/\\d{4}$")) {
            throw new ParametroInvalidoException("Validade do cartao invalida.");
        }
    }

    public static int converterInteiroPositivo(String valorStr, String msgNulo,
                                               String msgNaoNumerico, String msgNaoPositivo) {
        validarNaoVazio(valorStr, msgNulo);
        String texto = valorStr.trim();
        if (!texto.matches("^-?\\d+$")) {
            throw new ParametroInvalidoException(msgNaoNumerico);
        }
        int valor;
        try {
            valor = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new ParametroInvalidoException(msgNaoNumerico);
        }
        if (valor <= 0) {
            throw new ParametroInvalidoException(msgNaoPositivo);
        }
        return valor;
    }

    public static int converterNivelBateria(String nivelStr) {
        validarNaoVazio(nivelStr, "Nivel de bateria nao pode ser nulo.");
        String texto = nivelStr.trim();
        if (!texto.matches("^-?\\d+$")) {
            throw new ParametroInvalidoException("Nivel de bateria deve ser numerico.");
        }
        int nivel;
        try {
            nivel = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new ParametroInvalidoException("Nivel de bateria deve ser numerico.");
        }
        if (nivel < 0 || nivel > 100) {
            throw new ParametroInvalidoException("Nivel de bateria deve estar entre 0 e 100.");
        }
        return nivel;
    }

    public static LocalDateTime converterDataHora(String dataStr) {
        validarNaoVazio(dataStr, "Data nao pode ser nula.");
        try {
            return LocalDateTime.parse(dataStr.trim(), FormatadorDataMoeda.getFormatadorDataHora());
        } catch (DateTimeParseException e) {
            throw new ParametroInvalidoException("Data invalida.");
        }
    }
}
