package br.ufal.ic.p2.bikeshare.servicos;

import br.ufal.ic.p2.bikeshare.entidades.Bicicleta;
import br.ufal.ic.p2.bikeshare.entidades.BicicletaConvencional;
import br.ufal.ic.p2.bikeshare.entidades.BicicletaEletrica;
import br.ufal.ic.p2.bikeshare.entidades.Estacao;
import br.ufal.ic.p2.bikeshare.excecoes.BicicletaIndisponivelException;
import br.ufal.ic.p2.bikeshare.excecoes.EntidadeJaExisteException;
import br.ufal.ic.p2.bikeshare.excecoes.EstacaoSemVagasException;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoInvalidaPorTipoException;
import br.ufal.ic.p2.bikeshare.excecoes.ParametroNuloException;
import br.ufal.ic.p2.bikeshare.excecoes.TipoInvalidoException;
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

    public void adicionarBicicleta(String idEstacao, String idBicicleta, String tipo) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        Estacao estacao = repo.buscarEstacaoExistente(idEstacao);
        validarNovaBicicletaId(repo, idBicicleta);

        if (BicicletaEletrica.TIPO.equals(tipo)) {
            throw new ParametroNuloException("Nivel de bateria nao pode ser nulo.");
        }
        if (!BicicletaConvencional.TIPO.equals(tipo)) {
            throw new TipoInvalidoException("Tipo invalido.");
        }
        if (!estacao.temVagaDisponivel()) {
            throw new EstacaoSemVagasException("Estacao sem vagas disponiveis.");
        }

        Bicicleta novaBicicleta = new BicicletaConvencional(idBicicleta, estacao.getId());
        estacao.estacionarBicicleta(novaBicicleta);
        repo.adicionarBicicleta(novaBicicleta);
    }

    public void adicionarBicicleta(String idEstacao, String idBicicleta, String tipo, String nivelBateriaStr) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        Estacao estacao = repo.buscarEstacaoExistente(idEstacao);
        validarNovaBicicletaId(repo, idBicicleta);

        if (BicicletaConvencional.TIPO.equals(tipo)) {
            throw new OperacaoInvalidaPorTipoException("Bicicleta nao possui bateria.");
        }
        if (!BicicletaEletrica.TIPO.equals(tipo)) {
            throw new TipoInvalidoException("Tipo invalido.");
        }
        int nivelBateria = Validador.converterNivelBateria(nivelBateriaStr);
        if (!estacao.temVagaDisponivel()) {
            throw new EstacaoSemVagasException("Estacao sem vagas disponiveis.");
        }

        Bicicleta novaBicicleta = new BicicletaEletrica(idBicicleta, estacao.getId(), nivelBateria);
        estacao.estacionarBicicleta(novaBicicleta);
        repo.adicionarBicicleta(novaBicicleta);
    }

    public void removerBicicleta(String idEstacao, String idBicicleta) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        Estacao estacao = repo.buscarEstacaoExistente(idEstacao);
        Bicicleta bicicleta = repo.buscarBicicletaExistente(idBicicleta);

        if (!estacao.contemBicicleta(bicicleta.getId())) {
            throw new BicicletaIndisponivelException("Bicicleta nao esta nesta estacao.");
        }
        if (!"disponivel".equals(bicicleta.getEstado())) {
            throw new BicicletaIndisponivelException("Bicicleta nao esta disponivel.");
        }

        estacao.retirarBicicletaDaEstacao(bicicleta.getId());
        repo.removerBicicleta(bicicleta.getId());
    }

    public String getAtributoBicicleta(String idBicicleta, String atributo) {
        Bicicleta bicicleta = gerenciador.getRepositorio().buscarBicicletaExistente(idBicicleta);
        return bicicleta.getAtributo(atributo);
    }

    public String getEstadoBicicleta(String idBicicleta) {
        Bicicleta bicicleta = gerenciador.getRepositorio().buscarBicicletaExistente(idBicicleta);
        return bicicleta.getEstado();
    }

    public String getVagasDisponiveis(String idEstacao) {
        Estacao estacao = gerenciador.getRepositorio().buscarEstacaoExistente(idEstacao);
        return String.valueOf(estacao.getVagasDisponiveis());
    }

    public String getBicicletasDisponiveis(String idEstacao) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        Estacao estacao = repo.buscarEstacaoExistente(idEstacao);
        int nivelMinimo = repo.getConfiguracao().getNivelMinimoBateriaEletrica();
        return String.valueOf(estacao.getBicicletasDisponiveis(nivelMinimo));
    }

    private void validarNovaBicicletaId(RepositorioSistema repo, String idBicicleta) {
        Validador.validarNaoVazio(idBicicleta, "Identificacao da bicicleta nao pode ser nula.");
        if (repo.buscarBicicleta(idBicicleta) != null) {
            throw new EntidadeJaExisteException("Bicicleta ja existe.");
        }
    }
}
