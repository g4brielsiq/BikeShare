package br.ufal.ic.p2.bikeshare.entidades;

import br.ufal.ic.p2.bikeshare.excecoes.OperacaoInvalidaPorTipoException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BicicletaConvencional extends Bicicleta {
    private static final long serialVersionUID = 1L;

    public static final String TIPO = "convencional";

    public BicicletaConvencional(String id, String idEstacaoAtual) {
        super(id, idEstacaoAtual);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getNivelBateriaFormatado() {
        throw new OperacaoInvalidaPorTipoException("Bicicleta nao possui bateria.");
    }

    @Override
    public boolean possuiBateriaSuficienteParaRetirada(int nivelMinimoBateria) {
        return true;
    }

    @Override
    public BigDecimal calcularAdicionalEletrica(long minutos, BigDecimal taxaPorMinuto) {
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }
}
