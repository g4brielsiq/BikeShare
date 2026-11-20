package br.ufal.ic.p2.bikeshare.servicos;

import br.ufal.ic.p2.bikeshare.entidades.Aluguel;
import br.ufal.ic.p2.bikeshare.entidades.Bicicleta;
import br.ufal.ic.p2.bikeshare.entidades.Estacao;
import br.ufal.ic.p2.bikeshare.entidades.Usuario;
import br.ufal.ic.p2.bikeshare.excecoes.BicicletaIndisponivelException;
import br.ufal.ic.p2.bikeshare.excecoes.EntidadeNaoEncontradaException;
import br.ufal.ic.p2.bikeshare.repositorio.RepositorioSistema;
import br.ufal.ic.p2.bikeshare.util.Validador;

import java.time.LocalDateTime;

public class ServicoAluguel {

    private final GerenciadorPersistenciaHistorico gerenciador;

    public ServicoAluguel(GerenciadorPersistenciaHistorico gerenciador) {
        this.gerenciador = gerenciador;
    }

    public String retirarBicicleta(String idUsuario, String idEstacao, String idBicicleta, String dataStr) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        Usuario usuario = buscarUsuarioExistente(repo, idUsuario);
        Estacao estacao = buscarEstacaoExistente(repo, idEstacao);
        Bicicleta bicicleta = buscarBicicletaExistente(repo, idBicicleta);
        LocalDateTime dataHoraInicio = Validador.converterDataHora(dataStr);

        usuario.validarSituacaoParaRetirada();
        usuario.validarMetodoPagamentoParaRetirada();

        if (!estacao.contemBicicleta(bicicleta.getId())) {
            throw new BicicletaIndisponivelException("Bicicleta nao esta nesta estacao.");
        }
        if (!"disponivel".equals(bicicleta.getEstado())) {
            throw new BicicletaIndisponivelException("Bicicleta nao esta disponivel.");
        }
        int nivelMinimoBateria = repo.getConfiguracao().getNivelMinimoBateriaEletrica();
        bicicleta.validarBateriaParaRetirada(nivelMinimoBateria);

        String idAluguel = repo.gerarProximoIdAluguel();
        Aluguel novoAluguel = new Aluguel(idAluguel, usuario.getId(), bicicleta.getId(), estacao.getId(), dataHoraInicio);
        estacao.liberarBicicletaParaAluguel(bicicleta);
        usuario.definirAluguelAtivo(novoAluguel.getId());
        repo.adicionarAluguel(novoAluguel);
        return idAluguel;
    }

    public String getAtributoAluguel(String idAluguel, String atributo) {
        Aluguel aluguel = buscarAluguelExistente(gerenciador.getRepositorio(), idAluguel);
        return aluguel.getAtributo(atributo);
    }

    private Usuario buscarUsuarioExistente(RepositorioSistema repo, String idUsuario) {
        Validador.validarNaoVazio(idUsuario, "Identificacao do usuario nao pode ser nula.");
        Usuario usuario = repo.buscarUsuario(idUsuario);
        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuario nao existe.");
        }
        return usuario;
    }

    private Estacao buscarEstacaoExistente(RepositorioSistema repo, String idEstacao) {
        Validador.validarNaoVazio(idEstacao, "Identificacao da estacao nao pode ser nula.");
        Estacao estacao = repo.buscarEstacao(idEstacao);
        if (estacao == null) {
            throw new EntidadeNaoEncontradaException("Estacao nao existe.");
        }
        return estacao;
    }

    private Bicicleta buscarBicicletaExistente(RepositorioSistema repo, String idBicicleta) {
        Validador.validarNaoVazio(idBicicleta, "Identificacao da bicicleta nao pode ser nula.");
        Bicicleta bicicleta = repo.buscarBicicleta(idBicicleta);
        if (bicicleta == null) {
            throw new EntidadeNaoEncontradaException("Bicicleta nao existe.");
        }
        return bicicleta;
    }

    private Aluguel buscarAluguelExistente(RepositorioSistema repo, String idAluguel) {
        Validador.validarNaoVazio(idAluguel, "Identificacao do aluguel nao pode ser nula.");
        Aluguel aluguel = repo.buscarAluguel(idAluguel);
        if (aluguel == null) {
            throw new EntidadeNaoEncontradaException("Aluguel nao existe.");
        }
        return aluguel;
    }
}
