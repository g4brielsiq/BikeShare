package br.ufal.ic.p2.bikeshare.excecoes;

public class AluguelEmAndamentoException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public AluguelEmAndamentoException(String mensagem) {
        super(mensagem);
    }
}
