package br.ufal.ic.p2.bikeshare.entidades;

import java.math.BigDecimal;

public class PenalidadeAtraso extends Penalidade {
    private static final long serialVersionUID = 1L;

    public static final String TIPO = "atraso";

    public PenalidadeAtraso(BigDecimal valor) {
        super(valor, "Devolucao com atraso acima de 24h.");
    }

    @Override
    public String getTipo() {
        return TIPO;
    }
}
