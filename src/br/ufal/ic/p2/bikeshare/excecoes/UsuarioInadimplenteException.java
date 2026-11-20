package br.ufal.ic.p2.bikeshare.excecoes;

public class UsuarioInadimplenteException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public UsuarioInadimplenteException(String mensagem) {
        super(mensagem);
    }
}
