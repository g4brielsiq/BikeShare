package br.ufal.ic.p2.bikeshare.excecoes;

public class ParametroNuloException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public ParametroNuloException(String mensagem) {
        super(mensagem);
    }
}
