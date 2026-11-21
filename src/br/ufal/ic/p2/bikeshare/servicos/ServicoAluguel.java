package br.ufal.ic.p2.bikeshare.servicos;

import br.ufal.ic.p2.bikeshare.entidades.Aluguel;
import br.ufal.ic.p2.bikeshare.entidades.Bicicleta;
import br.ufal.ic.p2.bikeshare.entidades.ConfiguracaoSistema;
import br.ufal.ic.p2.bikeshare.entidades.Estacao;
import br.ufal.ic.p2.bikeshare.entidades.PenalidadeAtraso;
import br.ufal.ic.p2.bikeshare.entidades.PlanoTarifacao;
import br.ufal.ic.p2.bikeshare.entidades.Usuario;
import br.ufal.ic.p2.bikeshare.excecoes.AluguelEmAndamentoException;
import br.ufal.ic.p2.bikeshare.excecoes.BicicletaIndisponivelException;
import br.ufal.ic.p2.bikeshare.excecoes.EstacaoSemVagasException;
import br.ufal.ic.p2.bikeshare.excecoes.ParametroInvalidoException;
import br.ufal.ic.p2.bikeshare.repositorio.RepositorioSistema;
import br.ufal.ic.p2.bikeshare.util.Validador;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class ServicoAluguel {

    private final GerenciadorPersistenciaHistorico gerenciador;

    public ServicoAluguel(GerenciadorPersistenciaHistorico gerenciador) {
        this.gerenciador = gerenciador;
    }

    public String retirarBicicleta(String idUsuario, String idEstacao, String idBicicleta, String dataStr) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        Usuario usuario = repo.buscarUsuarioExistente(idUsuario);
        Estacao estacao = repo.buscarEstacaoExistente(idEstacao);
        Bicicleta bicicleta = repo.buscarBicicletaExistente(idBicicleta);
        LocalDateTime dataHoraInicio = Validador.converterDataHora(dataStr);

        usuario.validarSituacaoParaRetirada();

        if (!estacao.contemBicicleta(bicicleta.getId())) {
            throw new BicicletaIndisponivelException("Bicicleta nao esta nesta estacao.");
        }
        if (!"disponivel".equals(bicicleta.getEstado())) {
            throw new BicicletaIndisponivelException("Bicicleta nao esta disponivel.");
        }
        int nivelMinimoBateria = repo.getConfiguracao().getNivelMinimoBateriaEletrica();
        bicicleta.validarBateriaParaRetirada(nivelMinimoBateria);

        usuario.validarMetodoPagamentoParaRetirada();

        String idAluguel = repo.gerarProximoIdAluguel();
        Aluguel novoAluguel = new Aluguel(idAluguel, usuario.getId(), bicicleta.getId(), estacao.getId(), dataHoraInicio);
        estacao.liberarBicicletaParaAluguel(bicicleta);
        usuario.definirAluguelAtivo(novoAluguel.getId());
        repo.adicionarAluguel(novoAluguel);
        return idAluguel;
    }

    public void devolverBicicleta(String idAluguel, String idEstacao, String dataStr) {
        RepositorioSistema repo = gerenciador.getRepositorio();
        Aluguel aluguel = repo.buscarAluguelExistente(idAluguel);
        Estacao estacaoDestino = repo.buscarEstacaoExistente(idEstacao);
        LocalDateTime dataHoraFim = Validador.converterDataHora(dataStr);

        if (!aluguel.isEmAndamento()) {
            throw new AluguelEmAndamentoException("Aluguel nao esta em andamento.");
        }
        if (dataHoraFim.isBefore(aluguel.getInicio())) {
            throw new ParametroInvalidoException("Data de devolucao anterior a data de retirada.");
        }
        if (!estacaoDestino.temVagaDisponivel()) {
            throw new EstacaoSemVagasException("Estacao cheia.");
        }

        long minutos = Duration.between(aluguel.getInicio(), dataHoraFim).toMinutes();
        ConfiguracaoSistema config = repo.getConfiguracao();
        Usuario usuario = repo.buscarUsuario(aluguel.getIdUsuario());
        Bicicleta bicicleta = repo.buscarBicicleta(aluguel.getIdBicicleta());

        boolean mesmaEstacao = estacaoDestino.getId().equals(aluguel.getIdEstacaoOrigem());
        if (mesmaEstacao && minutos <= config.getCarenciaCancelamentoMinutos()) {
            aluguel.registrarConclusao(Aluguel.ESTADO_CANCELADO, estacaoDestino.getId(), dataHoraFim, BigDecimal.ZERO);
            estacaoDestino.receberBicicletaDevolvida(bicicleta, "disponivel");
            usuario.definirAluguelAtivo(null);
            return;
        }

        if (minutos > config.getTempoMaximoAluguelMinutos()) {
            aluguel.registrarConclusao(Aluguel.ESTADO_EXPIRADO, estacaoDestino.getId(), dataHoraFim, BigDecimal.ZERO);
            estacaoDestino.receberBicicletaDevolvida(bicicleta, "emManutencao");
            usuario.registrarPenalidade(new PenalidadeAtraso(config.getValorPenalidadeAtraso()));
            usuario.definirAluguelAtivo(null);
            return;
        }

        PlanoTarifacao plano = repo.buscarPlano(usuario.getPlano());
        BigDecimal valorBase = plano.calcularValorBase(minutos);
        BigDecimal valorAdicional = bicicleta.calcularAdicionalEletrica(minutos, config.getAdicionalMinutoEletrica());
        BigDecimal valorTotal = valorBase.add(valorAdicional);

        usuario.debitarValorCorrida(valorTotal);

        aluguel.registrarConclusao(Aluguel.ESTADO_FINALIZADO, estacaoDestino.getId(), dataHoraFim, valorTotal);
        estacaoDestino.receberBicicletaDevolvida(bicicleta, "disponivel");
        usuario.definirAluguelAtivo(null);
    }

    public String getAtributoAluguel(String idAluguel, String atributo) {
        Aluguel aluguel = gerenciador.getRepositorio().buscarAluguelExistente(idAluguel);
        return aluguel.getAtributo(atributo);
    }
}
