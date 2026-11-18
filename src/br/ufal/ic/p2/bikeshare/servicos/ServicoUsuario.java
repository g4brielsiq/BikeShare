package br.ufal.ic.p2.bikeshare.servicos;

import br.ufal.ic.p2.bikeshare.entidades.PessoaFisica;
import br.ufal.ic.p2.bikeshare.entidades.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.entidades.PlanoTarifacao;
import br.ufal.ic.p2.bikeshare.entidades.Usuario;
import br.ufal.ic.p2.bikeshare.excecoes.EntidadeJaExisteException;
import br.ufal.ic.p2.bikeshare.excecoes.EntidadeNaoEncontradaException;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoInvalidaPorTipoException;
import br.ufal.ic.p2.bikeshare.excecoes.ParametroInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.TipoInvalidoException;
import br.ufal.ic.p2.bikeshare.repositorio.RepositorioSistema;
import br.ufal.ic.p2.bikeshare.util.Validador;

import java.math.BigDecimal;

public class ServicoUsuario {

    private final GerenciadorPersistenciaHistorico gerenciador;

    public ServicoUsuario(GerenciadorPersistenciaHistorico gerenciador) {
        this.gerenciador = gerenciador;
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpfOuCnpj) {
        return criarUsuario(nome, email, senha, tipo, cpfOuCnpj, "avulso", "");
    }

    public String criarUsuario(String nome, String email, String senha, String tipo,
                               String cnpj, String razaoSocial) {
        return criarUsuario(nome, email, senha, tipo, cnpj, razaoSocial, "avulso", "");
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpf,
                               String plano, String mensalidade) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        validarDadosComuns(nome, email, senha, tipo, PessoaFisica.TIPO);
        Validador.validarNaoVazio(cpf, "CPF nao pode ser nulo.");
        validarEmailUnico(repo, email);

        PlanoTarifacao planoObj = validarPlano(repo, plano);
        BigDecimal valorMensalidade = validarMensalidadeNaCriacao(planoObj, mensalidade);

        String id = repo.gerarProximoIdUsuario();
        Usuario novoUsuario = new PessoaFisica(
                id, nome, email, senha, cpf,
                planoObj.getNome(), planoObj.isPossuiMensalidade(), valorMensalidade
        );
        repo.adicionarUsuario(novoUsuario);
        return id;
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                               String razaoSocial, String plano, String mensalidade) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        validarDadosComuns(nome, email, senha, tipo, PessoaJuridica.TIPO);
        Validador.validarNaoVazio(cnpj, "CNPJ nao pode ser nulo.");
        Validador.validarNaoVazio(razaoSocial, "Razao social nao pode ser nula.");
        validarEmailUnico(repo, email);

        PlanoTarifacao planoObj = validarPlano(repo, plano);
        BigDecimal valorMensalidade = validarMensalidadeNaCriacao(planoObj, mensalidade);

        String id = repo.gerarProximoIdUsuario();
        Usuario novoUsuario = new PessoaJuridica(
                id, nome, email, senha, cnpj, razaoSocial,
                planoObj.getNome(), planoObj.isPossuiMensalidade(), valorMensalidade
        );
        repo.adicionarUsuario(novoUsuario);
        return id;
    }

    public String getAtributoUsuario(String idUsuario, String atributo) {
        Validador.validarNaoVazio(idUsuario, "Identificacao do usuario nao pode ser nula.");
        Usuario usuario = gerenciador.getRepositorio().buscarUsuario(idUsuario);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuario nao existe.");
        }
        return usuario.getAtributo(atributo);
    }

    private void validarDadosComuns(String nome, String email, String senha,
                                    String tipoInformado, String tipoEsperado) {
        Validador.validarNaoVazio(nome, "Nome nao pode ser nulo.");
        Validador.validarEmail(email);
        Validador.validarNaoVazio(senha, "Senha nao pode ser nula.");
        if (!tipoEsperado.equals(tipoInformado)) {
            throw new TipoInvalidoException("Tipo invalido.");
        }
    }

    private void validarEmailUnico(RepositorioSistema repo, String email) {
        if (repo.existeEmail(email)) {
            throw new EntidadeJaExisteException("Email ja cadastrado.");
        }
    }

    private PlanoTarifacao validarPlano(RepositorioSistema repo, String nomePlano) {
        if (Validador.isVazio(nomePlano)) {
            throw new ParametroInvalidoException("Plano invalido.");
        }
        PlanoTarifacao planoObj = repo.buscarPlano(nomePlano);
        if (planoObj == null) {
            throw new ParametroInvalidoException("Plano invalido.");
        }
        return planoObj;
    }

    private BigDecimal validarMensalidadeNaCriacao(PlanoTarifacao planoObj, String mensalidadeStr) {
        if (!planoObj.isPossuiMensalidade()) {
            if (!Validador.isVazio(mensalidadeStr)) {
                throw new OperacaoInvalidaPorTipoException("Plano nao possui mensalidade.");
            }
            return BigDecimal.ZERO;
        }
        return Validador.converterDecimalNaoNegativo(
                mensalidadeStr,
                "Mensalidade nao pode ser nula.",
                "Mensalidade deve ser numerica.",
                "Mensalidade deve ser nao-negativa."
        );
    }
}
