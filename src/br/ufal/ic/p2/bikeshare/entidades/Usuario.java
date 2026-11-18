package br.ufal.ic.p2.bikeshare.entidades;

import br.ufal.ic.p2.bikeshare.excecoes.AtributoInexistenteException;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoInvalidaPorTipoException;
import br.ufal.ic.p2.bikeshare.util.FormatadorDataMoeda;
import br.ufal.ic.p2.bikeshare.util.Validador;

import java.io.Serializable;
import java.math.BigDecimal;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String nome;
    private String email;
    private String senha;
    private String estado;
    private String plano;
    private boolean possuiMensalidade;
    private BigDecimal mensalidade;
    private final SaldoPreCarregado carteiraSaldoPreCarregado;
    private MetodoPagamento metodoPagamentoAtivo;

    protected Usuario(String id, String nome, String email, String senha,
                      String plano, boolean possuiMensalidade, BigDecimal mensalidade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.estado = "ativo";
        this.plano = plano;
        this.possuiMensalidade = possuiMensalidade;
        this.mensalidade = mensalidade;
        this.carteiraSaldoPreCarregado = new SaldoPreCarregado();
        this.metodoPagamentoAtivo = null;
    }

    public abstract String getTipo();

    public abstract String getAtributoEspecifico(String atributo);

    public String getAtributo(String atributo) {
        if (Validador.isVazio(atributo)) {
            throw new AtributoInexistenteException("Atributo nao existe.");
        }
        switch (atributo) {
            case "nome":
                return nome;
            case "email":
                return email;
            case "senha":
                return senha;
            case "tipo":
                return getTipo();
            case "estado":
                return estado;
            case "plano":
                return plano;
            case "mensalidade":
                if (!possuiMensalidade) {
                    throw new OperacaoInvalidaPorTipoException("Plano nao possui mensalidade.");
                }
                return FormatadorDataMoeda.formatarMoeda(mensalidade);
            case "metodoPagamento":
                return metodoPagamentoAtivo == null ? "nenhum" : metodoPagamentoAtivo.getNomeMetodo();
            case "numeroCartao":
            case "validadeCartao":
            case "chavePix":
            case "saldo":
                if (metodoPagamentoAtivo == null) {
                    return obterErroSemMetodoPagamento(atributo);
                }
                return metodoPagamentoAtivo.getAtributo(atributo);
            case "aluguelAtivo":
                return "nenhum";
            case "totalPendencias":
                return FormatadorDataMoeda.formatarMoeda(BigDecimal.ZERO);
            case "cpf":
            case "cnpj":
            case "razaoSocial":
                return getAtributoEspecifico(atributo);
            default:
                throw new AtributoInexistenteException("Atributo nao existe.");
        }
    }

    private String obterErroSemMetodoPagamento(String atributo) {
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

    public void alterarNome(String novoNome) {
        this.nome = novoNome;
    }

    public void alterarEmail(String novoEmail) {
        this.email = novoEmail;
    }

    public void alterarSenha(String novaSenha) {
        this.senha = novaSenha;
    }

    public void alterarPlano(PlanoTarifacao novoPlano) {
        if (this.plano.equals(novoPlano.getNome())) {
            return;
        }
        this.plano = novoPlano.getNome();
        this.possuiMensalidade = novoPlano.isPossuiMensalidade();
        this.mensalidade = novoPlano.getMensalidadePadrao();
    }

    public void ativarSaldoPreCarregado() {
        this.metodoPagamentoAtivo = this.carteiraSaldoPreCarregado;
    }

    public void definirMetodoPagamento(MetodoPagamento novoMetodo) {
        this.metodoPagamentoAtivo = novoMetodo;
    }

    public void validarUsaSaldoPreCarregado() {
        if (metodoPagamentoAtivo == null || !SaldoPreCarregado.NOME_METODO.equals(metodoPagamentoAtivo.getNomeMetodo())) {
            throw new OperacaoInvalidaPorTipoException("Usuario nao usa saldo pre-carregado.");
        }
    }

    public void adicionarSaldo(BigDecimal valor) {
        validarUsaSaldoPreCarregado();
        this.metodoPagamentoAtivo.adicionarSaldo(valor);
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getEstado() {
        return estado;
    }

    public String getPlano() {
        return plano;
    }

    public boolean isPossuiMensalidade() {
        return possuiMensalidade;
    }

    public BigDecimal getMensalidade() {
        return mensalidade;
    }

    public MetodoPagamento getMetodoPagamentoAtivo() {
        return metodoPagamentoAtivo;
    }
}
