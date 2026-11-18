package br.ufal.ic.p2.bikeshare.entidades;

public class Cartao extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    public static final String NOME_METODO = "cartao";

    private final String numeroCartao;
    private final String validadeCartao;

    public Cartao(String numeroCartao, String validadeCartao) {
        this.numeroCartao = numeroCartao;
        this.validadeCartao = validadeCartao;
    }

    @Override
    public String getNomeMetodo() {
        return NOME_METODO;
    }

    @Override
    public String getAtributo(String atributo) {
        if ("numeroCartao".equals(atributo)) {
            return numeroCartao;
        }
        if ("validadeCartao".equals(atributo)) {
            return validadeCartao;
        }
        return super.getAtributo(atributo);
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public String getValidadeCartao() {
        return validadeCartao;
    }
}
