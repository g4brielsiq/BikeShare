package br.ufal.ic.p2.bikeshare.servicos;

import br.ufal.ic.p2.bikeshare.entidades.Cartao;
import br.ufal.ic.p2.bikeshare.entidades.PessoaFisica;
import br.ufal.ic.p2.bikeshare.entidades.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.entidades.Pix;
import br.ufal.ic.p2.bikeshare.entidades.PlanoTarifacao;
import br.ufal.ic.p2.bikeshare.entidades.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.entidades.Usuario;
import br.ufal.ic.p2.bikeshare.excecoes.AluguelEmAndamentoException;
import br.ufal.ic.p2.bikeshare.excecoes.AtributoNaoAlteravelException;
import br.ufal.ic.p2.bikeshare.excecoes.EntidadeJaExisteException;
import br.ufal.ic.p2.bikeshare.excecoes.EntidadeNaoEncontradaException;
import br.ufal.ic.p2.bikeshare.excecoes.MetodoPagamentoInvalidoException;
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
        Usuario usuario = gerenciador.getRepositorio().buscarUsuarioExistente(idUsuario);
        return usuario.getAtributo(atributo);
    }

    public String getUsuarioPorEmail(String email) {
        Validador.validarNaoVazio(email, "Email nao pode ser nulo.");
        Usuario usuario = gerenciador.getRepositorio().buscarUsuarioPorEmail(email);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuario nao existe.");
        }
        return usuario.getId();
    }

    public void alterarUsuario(String idUsuario, String atributo, String valor) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        Usuario usuario = repo.buscarUsuarioExistente(idUsuario);
        if (Validador.isVazio(atributo)) {
            throw new AtributoNaoAlteravelException("Atributo nao pode ser alterado.");
        }
        switch (atributo) {
            case "nome":
                Validador.validarNaoVazio(valor, "Nome nao pode ser nulo.");
                usuario.alterarNome(valor);
                break;
            case "email":
                Validador.validarEmail(valor);
                Usuario existente = repo.buscarUsuarioPorEmail(valor);
                if (existente != null && !existente.getId().equals(usuario.getId())) {
                    throw new EntidadeJaExisteException("Email ja cadastrado.");
                }
                usuario.alterarEmail(valor);
                break;
            case "senha":
                Validador.validarNaoVazio(valor, "Senha nao pode ser nula.");
                usuario.alterarSenha(valor);
                break;
            case "plano":
                PlanoTarifacao novoPlano = validarPlano(repo, valor);
                usuario.alterarPlano(novoPlano);
                break;
            case "metodoPagamento":
                if (!SaldoPreCarregado.NOME_METODO.equals(valor)) {
                    throw new MetodoPagamentoInvalidoException("Valor de metodo de pagamento invalido.");
                }
                usuario.ativarSaldoPreCarregado();
                break;
            case "saldo":
                usuario.validarUsaSaldoPreCarregado();
                BigDecimal acressimo = Validador.converterDecimalPositivo(
                        valor,
                        "Valor nao pode ser nulo.",
                        "Valor deve ser numerico.",
                        "Valor deve ser positivo."
                );
                usuario.adicionarSaldo(acressimo);
                break;
            default:
                throw new AtributoNaoAlteravelException("Atributo nao pode ser alterado.");
        }
    }

    public void alterarUsuario(String idUsuario, String atributo, String valor1, String chave) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        Usuario usuario = repo.buscarUsuarioExistente(idUsuario);
        if (!"metodoPagamento".equals(atributo)) {
            throw new AtributoNaoAlteravelException("Atributo nao pode ser alterado.");
        }
        if (!Pix.NOME_METODO.equals(valor1)) {
            throw new MetodoPagamentoInvalidoException("Valor de metodo de pagamento invalido.");
        }
        Validador.validarNaoVazio(chave, "Chave pix nao pode ser nula.");
        usuario.definirMetodoPagamento(new Pix(chave));
    }

    public void alterarUsuario(String idUsuario, String atributo, String valor1,
                               String numero, String validade) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        Usuario usuario = repo.buscarUsuarioExistente(idUsuario);
        if (!"metodoPagamento".equals(atributo)) {
            throw new AtributoNaoAlteravelException("Atributo nao pode ser alterado.");
        }
        if (!Cartao.NOME_METODO.equals(valor1)) {
            throw new MetodoPagamentoInvalidoException("Valor de metodo de pagamento invalido.");
        }
        Validador.validarNaoVazio(numero, "Numero do cartao nao pode ser nulo.");
        Validador.validarValidadeCartao(validade);
        usuario.definirMetodoPagamento(new Cartao(numero, validade));
    }

    public void removerUsuario(String idUsuario) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        Usuario usuario = repo.buscarUsuarioExistente(idUsuario);
        if (usuario.possuiAluguelEmAndamento()) {
            throw new AluguelEmAndamentoException("Nao e possivel remover usuario com aluguel em andamento.");
        }
        repo.removerUsuario(usuario.getId());
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
