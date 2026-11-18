package br.ufal.ic.p2.bikeshare.excecoes;

public class EntidadeNaoEncontradaException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
