package br.ufal.ic.p2.bikeshare.excecoes;

public class EntidadeJaExisteException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public EntidadeJaExisteException(String mensagem) {
        super(mensagem);
    }
}
