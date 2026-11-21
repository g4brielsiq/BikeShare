# BikeShare - Sistema de Bicicletas Compartilhadas

Projeto prático desenvolvido para a disciplina **Programação 2 (COMP372)** do curso de Ciência da Computação da **Universidade Federal de Alagoas (UFAL)**, ministrada pelo professor Mário Hozano.

<p align="center">
  <img src="https://user-images.githubusercontent.com/91018438/204195385-acc6fcd4-05a7-4f25-87d1-cb7d5cc5c852.png" alt="Instituto de Computação - UFAL" />
</p>

---

## Sobre o Projeto e Objetivos de Engenharia

O **BikeShare** consiste no núcleo de regras de negócio de uma plataforma de gerenciamento de bicicletas compartilhadas (*bike-sharing*), inspirada em serviços de mobilidade urbana como Bike Itaú e Tembici. O sistema administra o ciclo operacional completo entre **usuários** (pessoas físicas e jurídicas), **estações** físicas de compartilhamento, **bicicletas** (convencionais e elétricas) e **aluguéis** (corridas), operando de maneira totalmente desacoplada de interface gráfica por meio do padrão arquitetural **Façade** (`br.ufal.ic.p2.bikeshare.Facade`) e validado por suítes automatizadas de testes de aceitação (**EasyAccept**).

Desenvolvido sob os fundamentos de **Programação Orientada a Objetos (POO)** em Java, o projeto aplica na prática sete pilares de engenharia e modelagem de domínio:

1. **Abstração e Modelagem do Mundo Real:** Representação coesa das entidades de mobilidade urbana — usuários, estações com controle de capacidade física, frota de bicicletas convencionais e elétricas, métodos de pagamento, planos tarifários, corridas e penalidades.
2. **Encapsulamento, Atomicidade e Estado Consistente:** Proteção estrita das invariantes de domínio. Todas as pré-condições cadastrais, operacionais e financeiras são validadas integralmente antes de qualquer mutação de estado ou geração de identificador sequencial (`U1`, `E1`, `A1`), garantindo que falhas nunca produzam alterações parciais.
3. **Polimorfismo e Delegação Coesa:** Transferência das decisões e cálculos para as próprias hierarquias de domínio (`Usuario`, `MetodoPagamento`, `Bicicleta` e `Penalidade`), eliminando cadeias condicionais de verificação de tipo (`instanceof`) ou comparações de strings nos serviços.
4. **Composição vs. Herança:** Uso criterioso de herança para especializações autênticas do domínio (`PessoaFisica` e `PessoaJuridica`; `Cartao`, `Pix` e `SaldoPreCarregado`; `BicicletaConvencional` e `BicicletaEletrica`; `PenalidadeAtraso`) combinado à composição para associar planos de tarifação, carteira pré-carregada preservada e histórico de pendências.
5. **Hierarquia de Exceções Customizadas:** Sinalização semântica de violações de negócio por meio de classes de exceção específicas em arquivos individuais, todas derivadas da superclasse `BikeShareException` (`RuntimeException`), mantendo as assinaturas da `Facade` limpas e compatíveis com os contratos do EasyAccept.
6. **Persistência Transparente de Objetos:** Serialização e restauração integral do estado do sistema em arquivo binário (`bikeshare_data.ser`) utilizando `java.io.Serializable`, garantindo continuidade de dados e contadores entre execuções independentes da JVM.
7. **Desenvolvimento Guiado por Testes de Aceitação:** Validação funcional contínua cobrindo 100% dos cenários de sucesso, casos de fronteira, tratamento de erros e persistência.

---

## Glossário

| Termo | Definição |
|---|---|
| **Usuário** | Cliente cadastrado no sistema, identificado por um código único sequencial gerado automaticamente (`U1`, `U2`, ...). Habilitado a contratar planos e realizar aluguéis. |
| **Estação** | Ponto físico com número fixo de vagas (`capacidade`), onde bicicletas permanecem estacionadas para retirada e devolução (`E1`, `E2`, ...). |
| **Bicicleta** | Veículo compartilhado identificado unicamente, vinculado a uma estação enquanto estacionado ou marcado como `emUso` durante uma corrida. |
| **Vaga** | Espaço físico disponível em uma estação para receber uma bicicleta (`vagas livres = capacidade − bicicletas fisicamente estacionadas`). |
| **Aluguel (ou corrida)** | Período de utilização de uma bicicleta por um usuário (`A1`, `A2`, ...), compreendido entre o instante de retirada na estação de origem e a devolução na estação de destino. |
| **Plano** | Modalidade de tarifação associada ao usuário: *avulso* (tarifado integralmente por minuto de uso) ou *assinante* (mensalidade fixa com franquia de minutos gratuitos por corrida e cobrança apenas sobre minutos excedentes). |
| **Tarifa** | Valor monetário apurado no encerramento de um aluguel, calculado a partir do plano vigente do usuário, da duração da corrida e do eventual adicional de propulsão elétrica. |
| **Penalidade / taxa extra** | Encargo financeiro lançado sobre o usuário em decorrência de infrações operacionais (como expiração do tempo máximo de 24 horas de aluguel). |
| **Inadimplência** | Estado atribuído ao usuário que possui penalidades financeiras pendentes, bloqueando o início de novas retiradas. |
| **Estado da bicicleta** | Condição operacional do veículo na frota: `disponivel`, `emUso`, `emManutencao` ou removida. |
| **Estado do aluguel** | Situação do ciclo de vida de uma corrida: `emAndamento`, `finalizado`, `cancelado` (dentro da carência na mesma estação) ou `expirado` (acima de 24 horas). |
| **Bicicleta convencional / elétrica** | Categorias da frota suportadas pelo sistema; o modelo elétrico monitora nível percentual de bateria (`0%` a `100%`), exige carga mínima configurável (`15%`) para retirada e aplica taxa adicional por minuto. |
| **Usuário pessoa física / jurídica** | Perfis cadastrais suportados; pessoas físicas identificam-se por CPF, enquanto pessoas jurídicas registram CNPJ e razão social. |
| **Método de pagamento** | Instrumento financeiro ativo do usuário para liquidação das corridas: `cartao`, `pix` ou `saldoPreCarregado`. |

---

## Escopo Implementado (Milestone 1 — US1 a US9_1)

O sistema implementa o fluxo completo de gestão cadastral, controle de pátio, operação de corridas, tarifação e persistência:

- **US1 & US1.1 - Cadastro de Usuários:** Registro de pessoas físicas (`CPF`) e jurídicas (`CNPJ` e razão social) com adesão ao plano padrão `avulso` ou ao plano `assinante` com mensalidade contratada, validação de unicidade de e-mail (*case-insensitive*), geração sequencial de identificadores (`U1`, `U2`, ...) e persistência em disco.
- **US2 - Consulta de Usuários:** Inspeção polimórfica de atributos cadastrais, operacionais e financeiros (`getAtributoUsuario`) e localização de usuários por endereço de e-mail (`getUsuarioPorEmail`).
- **US3 & US3.1 - Alteração Cadastral e Métodos de Pagamento:** Atualização de dados de perfil, transição entre planos tarifários (com preservação da mensalidade contratada quando aplicável) e configuração polimórfica dos métodos de pagamento (`Cartao` com validação `MM/yyyy`, `Pix` e `SaldoPreCarregado` com recargas acumulativas e preservação automática do saldo ao alternar entre modalidades).
- **US4 & US4.1 - Remoção de Usuários:** Exclusão segura de usuários sem aluguel ativo, liberando o e-mail para novos cadastros sem reutilizar identificadores anteriores e mantendo íntegros os registros históricos de aluguéis concluídos.
- **US5 & US5.1 - Cadastro de Estações:** Criação de estações com nome, endereço, capacidade inteira estritamente positiva e identificadores sequenciais (`E1`, `E2`, ...).
- **US6 & US6.1 - Gestão de Frota nas Estações:** Inclusão, consulta de atributos e remoção de bicicletas `convencional` e `eletrica` (com nível de bateria entre `0%` e `100%`), validando integralmente os dados do veículo antes da verificação de vagas na estação.
- **US7 - Consulta de Disponibilidade em Tempo Real:** Apuração de vagas físicas livres (`getVagasDisponiveis`), estado individual (`getEstadoBicicleta`) e bicicletas aptas para retirada (`getBicicletasDisponiveis`), distinguindo ocupação física de vaga da disponibilidade operacional (bicicletas elétricas com bateria `< 15%` ou bicicletas `emManutencao` ocupam vaga física, mas não são contabilizadas como disponíveis para retirada).
- **US8 & US8.1 - Retirada de Bicicletas:** Abertura de aluguéis (`A1`, `A2`, ...) com validação estrita de data/hora (`d/M/uuuu H:mm` via `ResolverStyle.STRICT`), verificação de adimplência, bloqueio de corridas simultâneas, conferência de disponibilidade e bateria mínima na estação de origem e validação de saldo/método de pagamento.
- **US9 & US9.1 - Devolução de Bicicletas, Carência, Expiração e Tarifação:** Encerramento de aluguéis contemplando três regimes operacionais:
  1. **Cancelamento por carência:** devolução na mesma estação de origem em até `2` minutos (`estado = cancelado`, custo `R$ 0,00`);
  2. **Expiração por tempo máximo:** devolução após `1440` minutos (`24h`), marcando o aluguel como `expirado` (`R$ 0,00`), encaminhando a bicicleta automaticamente para `emManutencao` na estação de destino e aplicando `PenalidadeAtraso` (`R$ 10,00`), tornando o usuário `inadimplente`;
  3. **Finalização normal:** apuração da tarifa base pelo plano do usuário (`avulso` a `R$ 0,50/min`; `assinante` com franquia de `60` min e excedente a `R$ 0,30/min`), acréscimo do adicional de bicicleta elétrica (`R$ 0,20/min` sobre todos os minutos da corrida) e débito atômico no método de pagamento ativo.

---

## Arquitetura e Organização do Projeto

O código-fonte está organizado em pacotes coesos e de responsabilidade única:

```text
src/
├── Main.java
└── br/ufal/ic/p2/bikeshare/
    ├── Facade.java
    ├── entidades/
    │   ├── ConfiguracaoSistema.java           (Parâmetros configuráveis de tarifas e limites)
    │   ├── PlanoTarifacao.java                (Cálculo de franquia, excedente e mensalidade)
    │   ├── Usuario.java                       (Superclasse abstrata de usuários)
    │   ├── PessoaFisica.java                  (Especialização de usuário com CPF)
    │   ├── PessoaJuridica.java                (Especialização com CNPJ e razão social)
    │   ├── MetodoPagamento.java               (Superclasse abstrata de meios de pagamento)
    │   ├── Cartao.java                        (Pagamento via cartão de crédito)
    │   ├── Pix.java                           (Pagamento via chave Pix)
    │   ├── SaldoPreCarregado.java             (Carteira pré-carregada com controle de saldo)
    │   ├── Estacao.java                       (Gestão de capacidade física e bicicletas)
    │   ├── Bicicleta.java                     (Superclasse abstrata de bicicletas)
    │   ├── BicicletaConvencional.java         (Bicicleta convencional sem bateria)
    │   ├── BicicletaEletrica.java             (Bicicleta elétrica com bateria e sobretaxa)
    │   ├── Aluguel.java                       (Ciclo de vida e dados de uma corrida)
    │   ├── Penalidade.java                    (Superclasse abstrata de penalidades)
    │   └── PenalidadeAtraso.java              (Penalidade automática por expiração > 24h)
    ├── repositorio/
    │   └── RepositorioSistema.java            (Contêiner serializável, coleções e IDs sequenciais)
    ├── servicos/
    │   ├── GerenciadorPersistenciaHistorico.java (Serialização e recuperação em bikeshare_data.ser)
    │   ├── ServicoUsuario.java                (Regras de negócio de usuários, planos e pagamentos)
    │   ├── ServicoEstacaoBicicleta.java       (Regras de negócio de estações, frota e vagas)
    │   └── ServicoAluguel.java                (Orquestração de retiradas, devoluções e tarifas)
    ├── util/
    │   ├── Validador.java                     (Validações de strings, e-mails, datas e números)
    │   └── FormatadorDataMoeda.java           (Formatação monetária pt-BR e datas estritas)
    └── excecoes/
        ├── BikeShareException.java            (Superclasse base RuntimeException)
        ├── AluguelEmAndamentoException.java
        ├── AtributoInexistenteException.java
        ├── AtributoNaoAlteravelException.java
        ├── BateriaInsuficienteException.java
        ├── BicicletaIndisponivelException.java
        ├── EntidadeJaExisteException.java
        ├── EntidadeNaoEncontradaException.java
        ├── EstacaoSemVagasException.java
        ├── MetodoPagamentoInvalidoException.java
        ├── OperacaoInvalidaPorTipoException.java
        ├── ParametroInvalidoException.java
        ├── ParametroNuloException.java
        ├── SaldoInsuficienteException.java
        ├── TipoInvalidoException.java
        └── UsuarioInadimplenteException.java
```

---

## Componentes Principais

### `Facade`
Ponto de entrada único exigido pelo EasyAccept. Atua estritamente como fachada de delegação (uma instrução por método), encaminhando cada operação ao serviço responsável sem reter estado de domínio ou lógica de validação.

### `entidades`
Implementa o modelo de domínio rico do sistema por meio de quatro hierarquias polimórficas (`Usuario`, `MetodoPagamento`, `Bicicleta` e `Penalidade`), acompanhadas por `Estacao`, `Aluguel`, `PlanoTarifacao` e `ConfiguracaoSistema` (que centraliza tarifas, franquias, nível mínimo de bateria, tempo de carência, limite de expiração e valor de multa, evitando constantes fixas espalhadas no código).

### `repositorio`
`RepositorioSistema` concentra os mapas ordenados (`LinkedHashMap`) de usuários, planos, estações, bicicletas e aluguéis, além dos contadores sequenciais de identificadores (`U`, `E`, `A`) e buscas validadas de entidades existentes.

### `servicos`
- `GerenciadorPersistenciaHistorico`: Encapsula a leitura e a escrita do objeto `RepositorioSistema` em `bikeshare_data.ser` via `ObjectInputStream` e `ObjectOutputStream`, além do reset completo em `zerarSistema()`.
- `ServicoUsuario`: Coordena o cadastro de pessoas físicas e jurídicas, consultas de atributos e busca por e-mail, alterações cadastrais, troca de planos e métodos de pagamento e remoção segura de usuários.
- `ServicoEstacaoBicicleta`: Administra a criação de estações, adição e remoção de bicicletas convencionais e elétricas e consultas de disponibilidade de frota e vagas.
- `ServicoAluguel`: Orquestra as pré-condições de retirada e o processamento de devoluções, aplicando as regras de carência, expiração automática com envio para manutenção e cálculo tarifário com `BigDecimal`.

### `util`
`Validador` e `FormatadorDataMoeda` centralizam o *parsing* numérico com `BigDecimal` (`RoundingMode.HALF_UP`), validação de inteiros puros, validação de datas com `DateTimeFormatter` estrito (`ResolverStyle.STRICT`) e formatação monetária no padrão brasileiro (`0,00`).

### `excecoes`
Reúne a hierarquia de exceções de domínio encabeçada por `BikeShareException`, garantindo que cada categoria de erro possua sua própria classe em arquivo dedicado e preserve com exatidão as mensagens verificadas nos scripts de aceitação.
