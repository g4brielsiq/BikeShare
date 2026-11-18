package br.ufal.ic.p2.bikeshare.entidades;

import br.ufal.ic.p2.bikeshare.excecoes.AtributoInexistenteException;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoInvalidaPorTipoException;

import java.math.BigDecimal;

public class PessoaFisica extends Usuario {
    private static final long serialVersionUID = 1L;

    public static final String TIPO = "pessoaFisica";

    private final String cpf;

    public PessoaFisica(String id, String nome, String email, String senha,
                        String cpf, String plano, boolean possuiMensalidade, BigDecimal mensalidade) {
        super(id, nome, email, senha, plano, possuiMensalidade, mensalidade);
        this.cpf = cpf;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getAtributoEspecifico(String atributo) {
        switch (atributo) {
            case "cpf":
                return cpf;
            case "cnpj":
            case "razaoSocial":
                throw new OperacaoInvalidaPorTipoException("Usuario nao eh pessoa juridica.");
            default:
                throw new AtributoInexistenteException("Atributo nao existe.");
        }
    }

    public String getCpf() {
        return cpf;
    }
}
