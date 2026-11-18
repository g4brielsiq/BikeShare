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
                return "nenhum";
            case "numeroCartao":
            case "validadeCartao":
                throw new OperacaoInvalidaPorTipoException("Usuario nao usa cartao.");
            case "chavePix":
                throw new OperacaoInvalidaPorTipoException("Usuario nao usa pix.");
            case "saldo":
                throw new OperacaoInvalidaPorTipoException("Usuario nao usa saldo pre-carregado.");
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
}
