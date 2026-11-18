package br.ufal.ic.p2.bikeshare.util;

import br.ufal.ic.p2.bikeshare.excecoes.ParametroInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.ParametroNuloException;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
}
