package br.ufal.ic.p2.bikeshare.servicos;

import br.ufal.ic.p2.bikeshare.entidades.Estacao;
import br.ufal.ic.p2.bikeshare.excecoes.EntidadeNaoEncontradaException;
import br.ufal.ic.p2.bikeshare.repositorio.RepositorioSistema;
import br.ufal.ic.p2.bikeshare.util.Validador;

public class ServicoEstacaoBicicleta {

    private final GerenciadorPersistenciaHistorico gerenciador;

    public ServicoEstacaoBicicleta(GerenciadorPersistenciaHistorico gerenciador) {
        this.gerenciador = gerenciador;
    }

    public String criarEstacao(String nome, String endereco, String capacidadeStr) {
        Validador.validarNaoVazio(nome, "Nome nao pode ser nulo.");
        Validador.validarNaoVazio(endereco, "Endereco nao pode ser nulo.");
        int capacidade = Validador.converterInteiroPositivo(
                capacidadeStr,
                "Capacidade nao pode ser nula.",
                "Capacidade deve ser numerica.",
                "Capacidade deve ser positiva."
        );

        RepositorioSistema repo = gerenciador.getRepositorio();
        String id = repo.gerarProximoIdEstacao();
        Estacao novaEstacao = new Estacao(id, nome, endereco, capacidade);
        repo.adicionarEstacao(novaEstacao);
        return id;
    }

    public String getVagasDisponiveis(String idEstacao) {
        Estacao estacao = buscarEstacaoExistente(gerenciador.getRepositorio(), idEstacao);
        return String.valueOf(estacao.getVagasDisponiveis());
    }

    public String getBicicletasDisponiveis(String idEstacao) {
        Estacao estacao = buscarEstacaoExistente(gerenciador.getRepositorio(), idEstacao);
        return String.valueOf(estacao.getBicicletasDisponiveis());
    }

    private Estacao buscarEstacaoExistente(RepositorioSistema repo, String idEstacao) {
        Validador.validarNaoVazio(idEstacao, "Identificacao da estacao nao pode ser nula.");
        Estacao estacao = repo.buscarEstacao(idEstacao);
        if (estacao == null) {
            throw new EntidadeNaoEncontradaException("Estacao nao existe.");
        }
        return estacao;
    }
}
