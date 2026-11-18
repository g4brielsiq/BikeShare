package br.ufal.ic.p2.bikeshare.excecoes;

public class TipoInvalidoException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public TipoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
