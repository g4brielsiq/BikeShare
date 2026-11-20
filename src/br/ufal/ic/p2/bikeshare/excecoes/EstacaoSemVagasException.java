package br.ufal.ic.p2.bikeshare.excecoes;

public class EstacaoSemVagasException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public EstacaoSemVagasException(String mensagem) {
        super(mensagem);
    }
}
