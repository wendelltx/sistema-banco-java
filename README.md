# Sistema Bancário em Java

Este projeto consiste em um **Sistema Bancário** completo desenvolvido em Java, utilizando os conceitos fundamentais da Programação Orientada a Objetos (POO): **Abstração, Encapsulamento, Herança, Polimorfismo e Interfaces**. 

O sistema simula operações bancárias do dia a dia, gerenciando contas correntes (com cheque especial) e contas poupança (com rendimentos mensais), mantendo o histórico de transações de cada cliente e gerando relatórios consolidados em uma interface gráfica simples construída a partir de caixas de diálogo `JOptionPane`.

---

## 🚀 Tecnologias Utilizadas

- **Linguagem**: Java 21 (Compatível com versões Java 17+)
- **Interface Gráfica**: Java Swing (`javax.swing.JOptionPane` para caixas de diálogo interativas)
- **Estruturas de Dados**: `java.util.List` e `java.util.ArrayList` para armazenamento dinâmico em memória.
- **Formatação e Tempo**: `java.time.LocalDateTime` para timestamps exatos de transações e `java.lang.String.format` para padronização de moeda em `R$`.

---

## 🛠️ Como Compilar e Executar o Projeto

Siga os passos abaixo no seu terminal para compilar e rodar o projeto:

### 1. Pré-requisitos
Certifique-se de ter o JDK instalado (versão 17 ou superior) e configurado no PATH do seu sistema. Para verificar, digite:
```bash
java -version
javac -version
```

### 2. Compilação
A partir do diretório raiz do projeto (onde fica este `README.md`), execute o comando para compilar todas as classes do pacote `banco` e colocá-las em um diretório de saída `/bin`:
```bash
# No Windows (PowerShell) ou Linux/macOS:
javac -d bin -sourcepath src src/banco/app/SistemaBanco.java
```

### 3. Execução
Execute a classe principal para iniciar o menu interativo:
```bash
java -cp bin banco.app.SistemaBanco
```

---

## 📂 Descrição dos Pacotes e Responsabilidade das Classes

A estrutura de pacotes obedece rigorosamente às diretrizes exigidas, dividida em modelo, interface, serviço e aplicação:

```
src/
└── banco/
    ├── interfaces/
    │   └── Operavel.java          # Contrato obrigatório para operações financeiras
    ├── model/
    │   ├── Cliente.java           # Entidade titular da conta
    │   ├── ContaBancaria.java     # Classe base abstrata para as contas
    │   ├── ContaCorrente.java     # Conta com limite de cheque especial
    │   └── ContaPoupanca.java     # Conta com rendimento de juros
    ├── service/
    │   └── BancoService.java      # Gerenciamento de contas e regras de negócio
    └── app/
        └── SistemaBanco.java      # Classe de entrada com o menu em loop
```

### 1. Pacote `banco.interfaces`
- **`Operavel.java`**: Define a interface que obriga qualquer classe que a implemente a possuir os comportamentos de `depositar(double valor)`, `sacar(double valor)` e `exibirSaldo()`.

### 2. Pacote `banco.model`
- **`Cliente.java`**: Representa o titular da conta bancária. Guarda de forma encapsulada as informações de `nome`, `cpf` e `telefone`. Possui construtor parametrizado, getters e setters, e um método `toString()` customizado para exibição dos dados.
- **`ContaBancaria.java`**: Classe abstrata que implementa `Operavel`. É a base de todas as contas do sistema. Gerencia o `numeroConta`, o `titular` (associação com a classe `Cliente`), o `saldo`, o `limiteDiarioSaque` (limite diário de saque padrão de R$ 2.500,00), o total acumulado `valorSacadoHoje`, a data do último saque `dataUltimoSaque`, e um `historico` de transações. Possui lógica comum de depósito e saque (validando o limite diário), além do método abstrato `gerarExtrato()`.
- **`ContaCorrente.java`**: Herda de `ContaBancaria`. Adiciona a propriedade de `limiteChequeEspecial`. Sobrescreve o método `sacar(double valor)` para permitir a retirada de valores que excedam o saldo real, ativando o cheque especial até o limite estipulado. Sobrescreve `gerarExtrato()` exibindo detalhes específicos como o total disponível para saque (saldo + limite).
- **`ContaPoupanca.java`**: Herda de `ContaBancaria`. Adiciona a propriedade `taxaRendimentoMensal`. Implementa o método `calcularRendimento()`, `aplicarRendimento()` (que soma o rendimento ao saldo e registra no histórico) e sobrescreve `gerarExtrato()` mostrando o rendimento previsto para o próximo mês.

### 3. Pacote `banco.service`
- **`BancoService.java`**: Centraliza o armazenamento das contas em listas dinâmicas segregadas. É responsável por:
  - Cadastrar contas validando a unicidade do número (impedindo duplicatas);
  - Buscar contas pelo número;
  - Calcular o patrimônio total do banco;
  - Listar todas as contas cadastradas de forma amigável;
  - Exibir o relatório geral de gestão (totalizadores de contas por tipo, maior saldo, menor saldo e patrimônio total).

### 4. Pacote `banco.app`
- **`SistemaBanco.java`**: Ponto de entrada (`main`) da aplicação. Apresenta o menu interativo em `do-while` via `JOptionPane`. Possui rotinas para captura segura de dados (tratando `NumberFormatException` quando o usuário insere caracteres inválidos ou cancela a operação) e inclui uma rotina para **executar automaticamente o Roteiro de Testes Obrigatório** do professor (estendido para demonstrar também o controle de limite diário).

---

## 🌿 Diagrama Textual da Hierarquia de Classes

```
          <<interface>>
            Operavel
               ▲
               │
          (implementa)
               │
          [abstract]
         ContaBancaria ◀────────────── (associação) ─── Cliente
               ▲
               ├─── ContaCorrente (herda de ContaBancaria)
               └─── ContaPoupanca (herda de ContaBancaria)
```

---

## 🔒 Funcionalidades Adicionais de Segurança (Senior Dev)

### Limite Diário de Saque (Transações)
- **Objetivo**: Proteger o saldo do cliente contra saques excessivos no mesmo dia.
- **Funcionamento**: 
  - Toda conta é inicializada com um limite diário padrão de **R$ 2.500,00**.
  - O sistema monitora o acumulado sacado no dia atual (`valorSacadoHoje`) e compara a data do último saque com a data do sistema (`dataUltimoSaque`), reiniciando o acumulado automaticamente a cada novo dia.
  - Se um saque solicitado exceder o limite diário restante, a transação é bloqueada e uma mensagem de erro é exibida.
  - O limite diário de qualquer conta (Corrente ou Poupança) pode ser reajustado em tempo de execução através da opção **[12]** do menu principal.

---

## 👤 Identificação do Aluno

- **Nome**: Wendell Teixeira
- **Turma**: Informática 2º Período 
- **Instituição**: CEFET-MG
