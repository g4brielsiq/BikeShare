package br.ufal.ic.p2.bikeshare.entidades;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Estacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String nome;
    private final String endereco;
    private final int capacidade;
    private final Map<String, Bicicleta> bicicletasEstacionadas;

    public Estacao(String id, String nome, String endereco, int capacidade) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidade = capacidade;
        this.bicicletasEstacionadas = new LinkedHashMap<>();
    }

    public int getVagasDisponiveis() {
        return capacidade - bicicletasEstacionadas.size();
    }

    public boolean temVagaDisponivel() {
        return getVagasDisponiveis() > 0;
    }

    public int getBicicletasDisponiveis(int nivelMinimoBateria) {
        int contador = 0;
        for (Bicicleta b : bicicletasEstacionadas.values()) {
            if (b.isDisponivelParaRetirada(nivelMinimoBateria)) {
                contador++;
            }
        }
        return contador;
    }

    public boolean contemBicicleta(String idBicicleta) {
        return bicicletasEstacionadas.containsKey(idBicicleta);
    }

    public void estacionarBicicleta(Bicicleta bicicleta) {
        bicicletasEstacionadas.put(bicicleta.getId(), bicicleta);
        bicicleta.definirEstacaoAtual(this.id);
    }

    public void retirarBicicletaDaEstacao(String idBicicleta) {
        bicicletasEstacionadas.remove(idBicicleta);
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getCapacidade() {
        return capacidade;
    }
}
