package br.ufal.ic.p2.bikeshare.entidades;

import br.ufal.ic.p2.bikeshare.excecoes.AtributoInexistenteException;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoInvalidaPorTipoException;

import java.io.Serializable;
import java.math.BigDecimal;

public abstract class MetodoPagamento implements Serializable {
    private static final long serialVersionUID = 1L;

    public abstract String getNomeMetodo();

    public String getAtributo(String atributo) {
        switch (atributo) {
            case "numeroCartao":
            case "validadeCartao":
                throw new OperacaoInvalidaPorTipoException("Usuario nao usa cartao.");
            case "chavePix":
                throw new OperacaoInvalidaPorTipoException("Usuario nao usa pix.");
            case "saldo":
                throw new OperacaoInvalidaPorTipoException("Usuario nao usa saldo pre-carregado.");
            default:
                throw new AtributoInexistenteException("Atributo nao existe.");
        }
    }

    public void adicionarSaldo(BigDecimal valor) {
        throw new OperacaoInvalidaPorTipoException("Usuario nao usa saldo pre-carregado.");
    }

    public void validarDisponibilidadeParaRetirada() {
    }

    public void debitar(BigDecimal valor) {
    }
}
