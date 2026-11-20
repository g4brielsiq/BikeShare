package br.ufal.ic.p2.bikeshare.entidades;

import br.ufal.ic.p2.bikeshare.excecoes.AluguelEmAndamentoException;
import br.ufal.ic.p2.bikeshare.excecoes.AtributoInexistenteException;
import br.ufal.ic.p2.bikeshare.util.FormatadorDataMoeda;
import br.ufal.ic.p2.bikeshare.util.Validador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Aluguel implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ESTADO_EM_ANDAMENTO = "emAndamento";

    private final String id;
    private final String idUsuario;
    private final String idBicicleta;
    private final String idEstacaoOrigem;
    private final LocalDateTime inicio;
    private String estado;
    private String idEstacaoDestino;
    private LocalDateTime fim;
    private BigDecimal valorCobrado;

    public Aluguel(String id, String idUsuario, String idBicicleta,
                   String idEstacaoOrigem, LocalDateTime inicio) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idBicicleta = idBicicleta;
        this.idEstacaoOrigem = idEstacaoOrigem;
        this.inicio = inicio;
        this.estado = ESTADO_EM_ANDAMENTO;
        this.idEstacaoDestino = null;
        this.fim = null;
        this.valorCobrado = BigDecimal.ZERO;
    }

    public String getAtributo(String atributo) {
        if (Validador.isVazio(atributo)) {
            throw new AtributoInexistenteException("Atributo nao existe.");
        }
        switch (atributo) {
            case "estado":
                return estado;
            case "usuario":
                return idUsuario;
            case "bicicleta":
                return idBicicleta;
            case "estacaoOrigem":
                return idEstacaoOrigem;
            case "inicio":
                return FormatadorDataMoeda.formatarDataHora(inicio);
            case "fim":
                validarConcluido();
                return FormatadorDataMoeda.formatarDataHora(fim);
            case "estacaoDestino":
                validarConcluido();
                return idEstacaoDestino;
            case "valorCobrado":
                validarConcluido();
                return FormatadorDataMoeda.formatarMoeda(valorCobrado);
            default:
                throw new AtributoInexistenteException("Atributo nao existe.");
        }
    }

    private void validarConcluido() {
        if (ESTADO_EM_ANDAMENTO.equals(estado)) {
            throw new AluguelEmAndamentoException("Aluguel ainda esta em andamento.");
        }
    }

    public String getId() {
        return id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getIdBicicleta() {
        return idBicicleta;
    }

    public String getIdEstacaoOrigem() {
        return idEstacaoOrigem;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public String getEstado() {
        return estado;
    }

    public String getIdEstacaoDestino() {
        return idEstacaoDestino;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public BigDecimal getValorCobrado() {
        return valorCobrado;
    }
}
