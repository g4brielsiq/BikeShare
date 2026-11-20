package br.ufal.ic.p2.bikeshare.excecoes;

public class BicicletaIndisponivelException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public BicicletaIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
