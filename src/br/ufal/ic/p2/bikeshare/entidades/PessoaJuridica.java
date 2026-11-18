package br.ufal.ic.p2.bikeshare.entidades;

import br.ufal.ic.p2.bikeshare.excecoes.AtributoInexistenteException;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoInvalidaPorTipoException;

import java.math.BigDecimal;

public class PessoaJuridica extends Usuario {
    private static final long serialVersionUID = 1L;

    public static final String TIPO = "pessoaJuridica";

    private final String cnpj;
    private final String razaoSocial;

    public PessoaJuridica(String id, String nome, String email, String senha,
                          String cnpj, String razaoSocial, String plano,
                          boolean possuiMensalidade, BigDecimal mensalidade) {
        super(id, nome, email, senha, plano, possuiMensalidade, mensalidade);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getAtributoEspecifico(String atributo) {
        switch (atributo) {
            case "cnpj":
                return cnpj;
            case "razaoSocial":
                return razaoSocial;
            case "cpf":
                throw new OperacaoInvalidaPorTipoException("Usuario nao eh pessoa fisica.");
            default:
                throw new AtributoInexistenteException("Atributo nao existe.");
        }
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }
}
