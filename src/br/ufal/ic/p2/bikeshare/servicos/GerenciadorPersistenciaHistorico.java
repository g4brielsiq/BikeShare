package br.ufal.ic.p2.bikeshare.servicos;

import br.ufal.ic.p2.bikeshare.repositorio.RepositorioSistema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GerenciadorPersistenciaHistorico {

    private static final String ARQUIVO_PERSISTENCIA = "bikeshare_data.ser";

    private RepositorioSistema repositorio;

    public GerenciadorPersistenciaHistorico() {
        this.repositorio = carregarDoDisco();
    }

    public RepositorioSistema getRepositorio() {
        return repositorio;
    }

    public void zerarSistema() {
        this.repositorio = new RepositorioSistema();
        File arquivo = new File(ARQUIVO_PERSISTENCIA);
        if (arquivo.exists()) {
            arquivo.delete();
        }
    }

    public void encerrarSistema() {
        salvarNoDisco(this.repositorio);
    }

    private RepositorioSistema carregarDoDisco() {
        File arquivo = new File(ARQUIVO_PERSISTENCIA);
        if (!arquivo.exists()) {
            return new RepositorioSistema();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (RepositorioSistema) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new RepositorioSistema();
        }
    }

    private void salvarNoDisco(RepositorioSistema repo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PERSISTENCIA))) {
            oos.writeObject(repo);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao salvar dados do sistema.", e);
        }
    }
}
