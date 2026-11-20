package br.ufal.ic.p2.bikeshare.entidades;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BicicletaEletrica extends Bicicleta {
    private static final long serialVersionUID = 1L;

    public static final String TIPO = "eletrica";

    private int nivelBateria;

    public BicicletaEletrica(String id, String idEstacaoAtual, int nivelBateria) {
        super(id, idEstacaoAtual);
        this.nivelBateria = nivelBateria;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getNivelBateriaFormatado() {
        return String.valueOf(nivelBateria);
    }

    @Override
    public boolean possuiBateriaSuficienteParaRetirada(int nivelMinimoBateria) {
        return nivelBateria >= nivelMinimoBateria;
    }

    @Override
    public BigDecimal calcularAdicionalEletrica(long minutos, BigDecimal taxaPorMinuto) {
        return taxaPorMinuto.multiply(BigDecimal.valueOf(minutos)).setScale(2, RoundingMode.HALF_UP);
    }

    public int getNivelBateria() {
        return nivelBateria;
    }
}
