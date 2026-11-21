package br.ufal.ic.p2.bikeshare.entidades;

import br.ufal.ic.p2.bikeshare.excecoes.SaldoInsuficienteException;
import br.ufal.ic.p2.bikeshare.util.FormatadorDataMoeda;

import java.math.BigDecimal;

public class SaldoPreCarregado extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    public static final String NOME_METODO = "saldoPreCarregado";

    private BigDecimal saldo;

    public SaldoPreCarregado() {
        this.saldo = BigDecimal.ZERO;
    }

    @Override
    public String getNomeMetodo() {
        return NOME_METODO;
    }

    @Override
    public String getAtributo(String atributo) {
        if ("saldo".equals(atributo)) {
            return FormatadorDataMoeda.formatarMoeda(saldo);
        }
        return super.getAtributo(atributo);
    }

    @Override
    public void validarSuporteRecargaSaldo() {
    }

    @Override
    public void adicionarSaldo(BigDecimal valor) {
        this.saldo = this.saldo.add(valor);
    }

    @Override
    public void validarDisponibilidadeParaRetirada() {
        if (this.saldo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente.");
        }
    }

    @Override
    public void debitar(BigDecimal valor) {
        if (this.saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente.");
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
