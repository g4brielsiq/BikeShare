package br.ufal.ic.p2.bikeshare.excecoes;

public class ParametroInvalidoException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public ParametroInvalidoException(String mensagem) {
        super(mensagem);
    }
}
