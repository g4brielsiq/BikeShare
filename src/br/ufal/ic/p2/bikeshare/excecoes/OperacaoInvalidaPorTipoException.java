package br.ufal.ic.p2.bikeshare.excecoes;

public class OperacaoInvalidaPorTipoException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public OperacaoInvalidaPorTipoException(String mensagem) {
        super(mensagem);
    }
}
