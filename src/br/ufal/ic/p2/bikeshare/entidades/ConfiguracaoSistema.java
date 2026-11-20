package br.ufal.ic.p2.bikeshare.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

public class ConfiguracaoSistema implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal tarifaMinutoAvulso = new BigDecimal("0.50");
    private int franquiaMinutosAssinante = 60;
    private BigDecimal valorMinutoExcedenteAssinante = new BigDecimal("0.30");
    private BigDecimal mensalidadePadraoAssinante = new BigDecimal("30.00");
    private int nivelMinimoBateriaEletrica = 15;

    public BigDecimal getTarifaMinutoAvulso() {
        return tarifaMinutoAvulso;
    }

    public int getFranquiaMinutosAssinante() {
        return franquiaMinutosAssinante;
    }

    public BigDecimal getValorMinutoExcedenteAssinante() {
        return valorMinutoExcedenteAssinante;
    }

    public BigDecimal getMensalidadePadraoAssinante() {
        return mensalidadePadraoAssinante;
    }

    public int getNivelMinimoBateriaEletrica() {
        return nivelMinimoBateriaEletrica;
    }
}
