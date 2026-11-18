package br.ufal.ic.p2.bikeshare.entidades;

public class Pix extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    public static final String NOME_METODO = "pix";

    private final String chavePix;

    public Pix(String chavePix) {
        this.chavePix = chavePix;
    }

    @Override
    public String getNomeMetodo() {
        return NOME_METODO;
    }

    @Override
    public String getAtributo(String atributo) {
        if ("chavePix".equals(atributo)) {
            return chavePix;
        }
        return super.getAtributo(atributo);
    }

    public String getChavePix() {
        return chavePix;
    }
}
