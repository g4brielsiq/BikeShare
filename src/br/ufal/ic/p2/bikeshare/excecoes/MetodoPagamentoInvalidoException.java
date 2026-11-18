package br.ufal.ic.p2.bikeshare.excecoes;

public class MetodoPagamentoInvalidoException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public MetodoPagamentoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
