package br.ufal.ic.p2.bikeshare.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

public class PlanoTarifacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String nome;
    private final int franquiaMinutos;
    private final BigDecimal valorMinutoExcedente;
    private final boolean possuiMensalidade;
    private final BigDecimal mensalidadePadrao;

    public PlanoTarifacao(String nome, int franquiaMinutos, BigDecimal valorMinutoExcedente,
                          boolean possuiMensalidade, BigDecimal mensalidadePadrao) {
        this.nome = nome;
        this.franquiaMinutos = franquiaMinutos;
        this.valorMinutoExcedente = valorMinutoExcedente;
        this.possuiMensalidade = possuiMensalidade;
        this.mensalidadePadrao = mensalidadePadrao;
    }

    public static PlanoTarifacao criarPlanoAvulso(ConfiguracaoSistema config) {
        return new PlanoTarifacao(
                "avulso",
                0,
                config.getTarifaMinutoAvulso(),
                false,
                BigDecimal.ZERO
        );
    }

    public static PlanoTarifacao criarPlanoAssinante(ConfiguracaoSistema config) {
        return new PlanoTarifacao(
                "assinante",
                config.getFranquiaMinutosAssinante(),
                config.getValorMinutoExcedenteAssinante(),
                true,
                config.getMensalidadePadraoAssinante()
        );
    }

    public String getNome() {
        return nome;
    }

    public int getFranquiaMinutos() {
        return franquiaMinutos;
    }

    public BigDecimal getValorMinutoExcedente() {
        return valorMinutoExcedente;
    }

    public boolean isPossuiMensalidade() {
        return possuiMensalidade;
    }

    public BigDecimal getMensalidadePadrao() {
        return mensalidadePadrao;
    }
}
