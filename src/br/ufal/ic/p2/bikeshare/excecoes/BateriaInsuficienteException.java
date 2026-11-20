package br.ufal.ic.p2.bikeshare.excecoes;

public class BateriaInsuficienteException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public BateriaInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
