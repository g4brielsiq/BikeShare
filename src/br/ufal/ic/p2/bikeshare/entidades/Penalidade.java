package br.ufal.ic.p2.bikeshare.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Penalidade implements Serializable {
    private static final long serialVersionUID = 1L;

    private final BigDecimal valor;
    private final String motivo;

    protected Penalidade(BigDecimal valor, String motivo) {
        this.valor = valor.setScale(2, RoundingMode.HALF_UP);
        this.motivo = motivo;
    }

    public abstract String getTipo();

    public BigDecimal getValor() {
        return valor;
    }

    public String getMotivo() {
        return motivo;
    }
}
