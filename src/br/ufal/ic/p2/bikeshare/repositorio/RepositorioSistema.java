package br.ufal.ic.p2.bikeshare.repositorio;

import br.ufal.ic.p2.bikeshare.entidades.Aluguel;
import br.ufal.ic.p2.bikeshare.entidades.Bicicleta;
import br.ufal.ic.p2.bikeshare.entidades.ConfiguracaoSistema;
import br.ufal.ic.p2.bikeshare.entidades.Estacao;
import br.ufal.ic.p2.bikeshare.entidades.PlanoTarifacao;
import br.ufal.ic.p2.bikeshare.entidades.Usuario;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class RepositorioSistema implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ConfiguracaoSistema configuracao;
    private final Map<String, Usuario> usuarios;
    private final Map<String, PlanoTarifacao> planos;
    private final Map<String, Estacao> estacoes;
    private final Map<String, Bicicleta> bicicletas;
    private final Map<String, Aluguel> alugueis;
    private int contadorUsuarios;
    private int contadorEstacoes;
    private int contadorAlugueis;

    public RepositorioSistema() {
        this.configuracao = new ConfiguracaoSistema();
        this.usuarios = new LinkedHashMap<>();
        this.planos = new LinkedHashMap<>();
        this.estacoes = new LinkedHashMap<>();
        this.bicicletas = new LinkedHashMap<>();
        this.alugueis = new LinkedHashMap<>();
        this.contadorUsuarios = 0;
        this.contadorEstacoes = 0;
        this.contadorAlugueis = 0;

        PlanoTarifacao avulso = PlanoTarifacao.criarPlanoAvulso(configuracao);
        PlanoTarifacao assinante = PlanoTarifacao.criarPlanoAssinante(configuracao);
        this.planos.put(avulso.getNome(), avulso);
        this.planos.put(assinante.getNome(), assinante);
    }

    public String gerarProximoIdUsuario() {
        contadorUsuarios++;
        return "U" + contadorUsuarios;
    }

    public void adicionarUsuario(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    public void removerUsuario(String id) {
        usuarios.remove(id);
    }

    public Usuario buscarUsuario(String id) {
        return usuarios.get(id);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        for (Usuario u : usuarios.values()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public boolean existeEmail(String email) {
        return buscarUsuarioPorEmail(email) != null;
    }

    public PlanoTarifacao buscarPlano(String nomePlano) {
        if (nomePlano == null) {
            return null;
        }
        return planos.get(nomePlano);
    }

    public String gerarProximoIdEstacao() {
        contadorEstacoes++;
        return "E" + contadorEstacoes;
    }

    public void adicionarEstacao(Estacao estacao) {
        estacoes.put(estacao.getId(), estacao);
    }

    public Estacao buscarEstacao(String id) {
        return estacoes.get(id);
    }

    public void adicionarBicicleta(Bicicleta bicicleta) {
        bicicletas.put(bicicleta.getId(), bicicleta);
    }

    public Bicicleta buscarBicicleta(String id) {
        return bicicletas.get(id);
    }

    public void removerBicicleta(String id) {
        bicicletas.remove(id);
    }

    public String gerarProximoIdAluguel() {
        contadorAlugueis++;
        return "A" + contadorAlugueis;
    }

    public void adicionarAluguel(Aluguel aluguel) {
        alugueis.put(aluguel.getId(), aluguel);
    }

    public Aluguel buscarAluguel(String id) {
        return alugueis.get(id);
    }

    public ConfiguracaoSistema getConfiguracao() {
        return configuracao;
    }
}
