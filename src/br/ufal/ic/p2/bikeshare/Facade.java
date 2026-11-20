package br.ufal.ic.p2.bikeshare;

import br.ufal.ic.p2.bikeshare.servicos.GerenciadorPersistenciaHistorico;
import br.ufal.ic.p2.bikeshare.servicos.ServicoEstacaoBicicleta;
import br.ufal.ic.p2.bikeshare.servicos.ServicoUsuario;

/**
 Facade
 */
public class Facade {

    private static final String AINDA_NAO_IMPLEMENTADO = "Ainda não implementado.";

    private final GerenciadorPersistenciaHistorico gerenciador;
    private final ServicoUsuario servicoUsuario;
    private final ServicoEstacaoBicicleta servicoEstacaoBicicleta;

    public Facade() {
        this.gerenciador = new GerenciadorPersistenciaHistorico();
        this.servicoUsuario = new ServicoUsuario(this.gerenciador);
        this.servicoEstacaoBicicleta = new ServicoEstacaoBicicleta(this.gerenciador);
    }

    // =====================================================================
    // US1 - Cadastro de usuario
    // =====================================================================

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpfOuCnpj) {
        return servicoUsuario.criarUsuario(nome, email, senha, tipo, cpfOuCnpj);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj, String razaoSocial) {
        return servicoUsuario.criarUsuario(nome, email, senha, tipo, cnpj, razaoSocial);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpf,
                                String plano, String mensalidade) {
        return servicoUsuario.criarUsuario(nome, email, senha, tipo, cpf, plano, mensalidade);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                                String razaoSocial, String plano, String mensalidade) {
        return servicoUsuario.criarUsuario(nome, email, senha, tipo, cnpj, razaoSocial, plano, mensalidade);
    }

    // =====================================================================
    // US2 - Consulta de usuario
    // =====================================================================

    public String getAtributoUsuario(String usuario, String atributo) {
        return servicoUsuario.getAtributoUsuario(usuario, atributo);
    }

    public String getUsuarioPorEmail(String email) {
        return servicoUsuario.getUsuarioPorEmail(email);
    }

    // =====================================================================
    // US3 - Alteracao de usuario (inclui metodo de pagamento)
    // =====================================================================

    public void alterarUsuario(String usuario, String atributo, String valor) {
        servicoUsuario.alterarUsuario(usuario, atributo, valor);
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String chave) {
        servicoUsuario.alterarUsuario(usuario, atributo, valor1, chave);
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String numero, String validade) {
        servicoUsuario.alterarUsuario(usuario, atributo, valor1, numero, validade);
    }

    // =====================================================================
    // US4 - Remocao de usuario
    // =====================================================================

    public void removerUsuario(String usuario) {
        servicoUsuario.removerUsuario(usuario);
    }

    // =====================================================================
    // US5 - Cadastro de estacao
    // =====================================================================

    public String criarEstacao(String nome, String endereco, String capacidade) {
        return servicoEstacaoBicicleta.criarEstacao(nome, endereco, capacidade);
    }

    // =====================================================================
    // US6 - Gestao de bicicletas na estacao
    // =====================================================================

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo) {
        servicoEstacaoBicicleta.adicionarBicicleta(estacao, bicicleta, tipo);
    }

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo, String nivelBateria) {
        servicoEstacaoBicicleta.adicionarBicicleta(estacao, bicicleta, tipo, nivelBateria);
    }

    public void removerBicicleta(String estacao, String bicicleta) {
        servicoEstacaoBicicleta.removerBicicleta(estacao, bicicleta);
    }

    public String getAtributoBicicleta(String bicicleta, String atributo) {
        return servicoEstacaoBicicleta.getAtributoBicicleta(bicicleta, atributo);
    }

    // =====================================================================
    // US7 - Consulta de disponibilidade
    // =====================================================================

    public String getBicicletasDisponiveis(String estacao) {
        return servicoEstacaoBicicleta.getBicicletasDisponiveis(estacao);
    }

    public String getVagasDisponiveis(String estacao) {
        return servicoEstacaoBicicleta.getVagasDisponiveis(estacao);
    }

    public String getEstadoBicicleta(String bicicleta) {
        return servicoEstacaoBicicleta.getEstadoBicicleta(bicicleta);
    }

    // =====================================================================
    // US8 - Retirada de bicicleta
    // =====================================================================

    public String retirarBicicleta(String usuario, String estacao, String bicicleta, String data) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US9 - Devolucao de bicicleta
    // =====================================================================

    public void devolverBicicleta(String aluguel, String estacao, String data) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US10 - Consulta de historico de alugueis
    // =====================================================================

    public String getHistoricoAlugueis(String usuario) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public String getHistoricoAlugueis(String usuario, String atributo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public String getAtributoAluguel(String aluguel, String atributo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US11/US12 - Planos e tarifacao / criacao de novos planos
    // =====================================================================

    public String criarPlano(String nome, String franquiaMinutos, String valorMinutoExcedente, String mensalidade) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US13 - Regras de bloqueio / lancamento manual de penalidade
    // =====================================================================

    public void lancaPenalidade(String usuario, String tipo, String valor, String motivo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US14 - Lancamento e conclusao de manutencao
    // =====================================================================

    public void lancaDefeito(String bicicleta, String descricao) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void concluiManutencao(String bicicleta) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US15 - Relatorios
    // =====================================================================

    public String getTotalFaturado(String dataInicial, String dataFinal) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US16 - Undo/redo
    // =====================================================================

    public void undo() {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void redo() {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // Ciclo de vida do sistema
    // =====================================================================

    public void zerarSistema() {
        gerenciador.zerarSistema();
    }

    public void encerrarSistema() {
        gerenciador.encerrarSistema();
    }
}
