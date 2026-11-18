package br.ufal.ic.p2.bikeshare.excecoes;

public class AtributoNaoAlteravelException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public AtributoNaoAlteravelException(String mensagem) {
        super(mensagem);
    }
}
