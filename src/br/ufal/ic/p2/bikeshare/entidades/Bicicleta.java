package br.ufal.ic.p2.bikeshare.entidades;

import br.ufal.ic.p2.bikeshare.excecoes.AtributoInexistenteException;
import br.ufal.ic.p2.bikeshare.excecoes.BateriaInsuficienteException;
import br.ufal.ic.p2.bikeshare.util.Validador;

import java.io.Serializable;

public abstract class Bicicleta implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String estado;
    private String idEstacaoAtual;

    protected Bicicleta(String id, String idEstacaoAtual) {
        this.id = id;
        this.estado = "disponivel";
        this.idEstacaoAtual = idEstacaoAtual;
    }

    public abstract String getTipo();

    public abstract String getNivelBateriaFormatado();

    public abstract boolean possuiBateriaSuficienteParaRetirada(int nivelMinimoBateria);

    public boolean isDisponivelParaRetirada(int nivelMinimoBateria) {
        return "disponivel".equals(estado) && possuiBateriaSuficienteParaRetirada(nivelMinimoBateria);
    }

    public void validarBateriaParaRetirada(int nivelMinimoBateria) {
        if (!possuiBateriaSuficienteParaRetirada(nivelMinimoBateria)) {
            throw new BateriaInsuficienteException("Nivel de bateria insuficiente.");
        }
    }

    public String getAtributo(String atributo) {
        if (Validador.isVazio(atributo)) {
            throw new AtributoInexistenteException("Atributo nao existe.");
        }
        switch (atributo) {
            case "tipo":
                return getTipo();
            case "estado":
                return estado;
            case "estacao":
                return idEstacaoAtual == null ? "nenhuma" : idEstacaoAtual;
            case "nivelBateria":
                return getNivelBateriaFormatado();
            default:
                throw new AtributoInexistenteException("Atributo nao existe.");
        }
    }

    public String getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public String getIdEstacaoAtual() {
        return idEstacaoAtual;
    }

    public void definirEstado(String novoEstado) {
        this.estado = novoEstado;
    }

    public void definirEstacaoAtual(String idEstacaoAtual) {
        this.idEstacaoAtual = idEstacaoAtual;
    }
}
