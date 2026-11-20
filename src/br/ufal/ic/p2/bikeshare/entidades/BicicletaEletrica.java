package br.ufal.ic.p2.bikeshare.entidades;

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

    public int getNivelBateria() {
        return nivelBateria;
    }
}
