package banco.app;

import javax.swing.JOptionPane;

import banco.model.Cliente;
import banco.model.ContaBancaria;
import banco.model.ContaCorrente;
import banco.model.ContaPoupanca;
import banco.service.BancoService;

public class SistemaBanco {
    public static void main(String[] args) {
        BancoService service = new BancoService();
        prePopularDados(service);

        int opcao = -1;
        do {
            StringBuilder menu = new StringBuilder();
            menu.append("<html><body style='font-family: Arial, sans-serif; width: 280px;'>")
                .append("<h2 style='color: #2e6da4; margin-top:0; border-bottom: 2px solid #2e6da4; padding-bottom: 3px;'>Sistema Bancário</h2>")
                .append("<p style='font-weight: bold; margin-bottom: 5px;'>Selecione uma opção:</p>")
                .append("<table border='0' style='font-size: 11px;'>")
                .append("<tr><td><b>[1]</b></td><td>Cadastrar Conta Corrente</td></tr>")
                .append("<tr><td><b>[2]</b></td><td>Cadastrar Conta Poupança</td></tr>")
                .append("<tr><td><b>[3]</b></td><td>Depositar</td></tr>")
                .append("<tr><td><b>[4]</b></td><td>Sacar</td></tr>")
                .append("<tr><td><b>[5]</b></td><td>Consultar Saldo</td></tr>")
                .append("<tr><td><b>[6]</b></td><td>Exibir Extrato da Conta</td></tr>")
                .append("<tr><td><b>[7]</b></td><td>Exibir Histórico de Transações</td></tr>")
                .append("<tr><td><b>[8]</b></td><td>Listar Todas as Contas</td></tr>")
                .append("<tr><td><b>[9]</b></td><td>Relatório Geral do Banco</td></tr>")
                .append("<tr><td colspan='2' style='border-top: 1px dotted #ccc; height: 5px;'></td></tr>")
                .append("<tr><td><b style='color: #a0522d;'>[10]</b></td><td style='color: #a0522d;'>Aplicar Rendimento (Poupança)</td></tr>")
                .append("<tr><td><b style='color: #a0522d;'>[11]</b></td><td style='color: #a0522d;'>Alterar Limite (Corrente)</td></tr>")
                .append("<tr><td><b style='color: #a0522d;'>[12]</b></td><td style='color: #a0522d;'>Alterar Limite Diário de Saque</td></tr>")
                .append("<tr><td><b style='color: #4cae4c;'>[13]</b></td><td style='color: #4cae4c; font-weight: bold;'>Executar Roteiro de Testes</td></tr>")
                .append("<tr><td><b>[0]</b></td><td><b>Encerrar Sistema</b></td></tr>")
                .append("</table>")
                .append("</body></html>");

            Integer escolha = lerInteiro(menu.toString(), "Menu Principal");
            if (escolha == null) {
                opcao = 0;
                continue;
            }

            opcao = escolha;

            switch (opcao) {
                case 1:
                    cadastrarContaCorrenteMenu(service);
                    break;
                case 2:
                    cadastrarContaPoupancaMenu(service);
                    break;
                case 3:
                    depositarMenu(service);
                    break;
                case 4:
                    sacarMenu(service);
                    break;
                case 5:
                    consultarSaldoMenu(service);
                    break;
                case 6:
                    exibirExtratoMenu(service);
                    break;
                case 7:
                    exibirHistoricoMenu(service);
                    break;
                case 8:
                    service.listarTodasAsContas();
                    break;
                case 9:
                    service.exibirRelatorioGeral();
                    break;
                case 10:
                    aplicarRendimentoMenu(service);
                    break;
                case 11:
                    alterarLimiteMenu(service);
                    break;
                case 12:
                    alterarLimiteDiarioMenu(service);
                    break;
                case 13:
                    executarRoteiroTestes(service);
                    break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Obrigado por utilizar o Sistema Bancário!", "Sistema Encerrado", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida! Escolha uma opção do menu.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (opcao != 0);
    }

    private static void prePopularDados(BancoService service) {
        Cliente c1 = new Cliente("Ana Silva", "123.456.789-01", "(11) 98765-4321");
        ContaCorrente cc1 = new ContaCorrente("1001-C", c1, 500.0, 1000.0);
        service.cadastrarContaSilencioso(cc1);

        Cliente c2 = new Cliente("Bruno Costa", "234.567.890-12", "(21) 97654-3210");
        ContaCorrente cc2 = new ContaCorrente("1002-C", c2, 1500.0, 500.0);
        service.cadastrarContaSilencioso(cc2);

        Cliente c3 = new Cliente("Carlos Souza", "345.678.901-23", "(31) 96543-2109");
        ContaPoupanca cp1 = new ContaPoupanca("2001-P", c3, 3000.0, 0.005);
        service.cadastrarContaSilencioso(cp1);

        Cliente c4 = new Cliente("Diana Santos", "456.789.012-34", "(47) 95432-1098");
        ContaPoupanca cp2 = new ContaPoupanca("2002-P", c4, 800.0, 0.007);
        service.cadastrarContaSilencioso(cp2);
    }

    private static void cadastrarContaCorrenteMenu(BancoService service) {
        String num = lerString("Digite o número da conta:", "Cadastro de Conta Corrente");
        if (num == null || num.isEmpty()) return;

        if (service.buscarConta(num) != null) {
            JOptionPane.showMessageDialog(null, "Erro: Já existe uma conta com este número!", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nome = lerString("Digite o nome do titular:", "Cadastro de Conta Corrente");
        if (nome == null || nome.isEmpty()) return;

        String cpf = lerString("Digite o CPF do titular:", "Cadastro de Conta Corrente");
        if (cpf == null || cpf.isEmpty()) return;

        String fone = lerString("Digite o telefone do titular:", "Cadastro de Conta Corrente");
        if (fone == null || fone.isEmpty()) return;

        Double saldo = lerDouble("Digite o saldo inicial (R$):", "Cadastro de Conta Corrente");
        if (saldo == null) return;
        if (saldo < 0) {
            JOptionPane.showMessageDialog(null, "Erro: O saldo inicial não pode ser negativo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double limite = lerDouble("Digite o limite do cheque especial (R$):", "Cadastro de Conta Corrente");
        if (limite == null) return;
        if (limite < 0) {
            JOptionPane.showMessageDialog(null, "Erro: O limite não pode ser negativo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = new Cliente(nome, cpf, fone);
        ContaCorrente cc = new ContaCorrente(num, cliente, saldo, limite);
        service.cadastrarContaCorrente(cc);
    }

    private static void cadastrarContaPoupancaMenu(BancoService service) {
        String num = lerString("Digite o número da conta:", "Cadastro de Conta Poupança");
        if (num == null || num.isEmpty()) return;

        if (service.buscarConta(num) != null) {
            JOptionPane.showMessageDialog(null, "Erro: Já existe uma conta com este número!", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nome = lerString("Digite o nome do titular:", "Cadastro de Conta Poupança");
        if (nome == null || nome.isEmpty()) return;

        String cpf = lerString("Digite o CPF do titular:", "Cadastro de Conta Poupança");
        if (cpf == null || cpf.isEmpty()) return;

        String fone = lerString("Digite o telefone do titular:", "Cadastro de Conta Poupança");
        if (fone == null || fone.isEmpty()) return;

        Double saldo = lerDouble("Digite o saldo inicial (R$):", "Cadastro de Conta Poupança");
        if (saldo == null) return;
        if (saldo < 0) {
            JOptionPane.showMessageDialog(null, "Erro: O saldo inicial não pode ser negativo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double taxa = lerDouble("Digite a taxa de rendimento mensal (ex: 0,005 para 0.5%):", "Cadastro de Conta Poupança");
        if (taxa == null) return;
        if (taxa < 0) {
            JOptionPane.showMessageDialog(null, "Erro: A taxa não pode ser negativa.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = new Cliente(nome, cpf, fone);
        ContaPoupanca cp = new ContaPoupanca(num, cliente, saldo, taxa);
        service.cadastrarContaPoupanca(cp);
    }

    private static void depositarMenu(BancoService service) {
        String num = lerString("Digite o número da conta para depósito:", "Depósito");
        if (num == null || num.isEmpty()) return;

        ContaBancaria conta = service.buscarConta(num);
        if (conta == null) {
            JOptionPane.showMessageDialog(null, "Erro: Conta não encontrada!", "Erro de Operação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double valor = lerDouble("Digite o valor do depósito (R$):", "Depósito");
        if (valor == null) return;

        conta.depositar(valor);
    }

    private static void sacarMenu(BancoService service) {
        String num = lerString("Digite o número da conta para saque:", "Saque");
        if (num == null || num.isEmpty()) return;

        ContaBancaria conta = service.buscarConta(num);
        if (conta == null) {
            JOptionPane.showMessageDialog(null, "Erro: Conta não encontrada!", "Erro de Operação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double valor = lerDouble("Digite o valor do saque (R$):", "Saque");
        if (valor == null) return;

        conta.sacar(valor);
    }

    private static void consultarSaldoMenu(BancoService service) {
        String num = lerString("Digite o número da conta:", "Consultar Saldo");
        if (num == null || num.isEmpty()) return;

        ContaBancaria conta = service.buscarConta(num);
        if (conta == null) {
            JOptionPane.showMessageDialog(null, "Erro: Conta não encontrada!", "Erro de Operação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        conta.exibirSaldo();
    }

    private static void exibirExtratoMenu(BancoService service) {
        String num = lerString("Digite o número da conta:", "Exibir Extrato");
        if (num == null || num.isEmpty()) return;

        ContaBancaria conta = service.buscarConta(num);
        if (conta == null) {
            JOptionPane.showMessageDialog(null, "Erro: Conta não encontrada!", "Erro de Operação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        conta.gerarExtrato();
    }

    private static void exibirHistoricoMenu(BancoService service) {
        String num = lerString("Digite o número da conta:", "Exibir Histórico de Transações");
        if (num == null || num.isEmpty()) return;

        ContaBancaria conta = service.buscarConta(num);
        if (conta == null) {
            JOptionPane.showMessageDialog(null, "Erro: Conta não encontrada!", "Erro de Operação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        conta.exibirHistorico();
    }

    private static void aplicarRendimentoMenu(BancoService service) {
        String num = lerString("Digite o número da Conta Poupança:", "Aplicar Rendimento");
        if (num == null || num.isEmpty()) return;

        ContaBancaria conta = service.buscarConta(num);
        if (conta == null) {
            JOptionPane.showMessageDialog(null, "Erro: Conta não encontrada!", "Erro de Operação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (conta instanceof ContaPoupanca) {
            ((ContaPoupanca) conta).aplicarRendimento();
        } else {
            JOptionPane.showMessageDialog(null, "Erro: Esta conta não é do tipo Poupança!", "Operação Negada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void alterarLimiteMenu(BancoService service) {
        String num = lerString("Digite o número da Conta Corrente:", "Alterar Limite do Cheque Especial");
        if (num == null || num.isEmpty()) return;

        ContaBancaria conta = service.buscarConta(num);
        if (conta == null) {
            JOptionPane.showMessageDialog(null, "Erro: Conta não encontrada!", "Erro de Operação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (conta instanceof ContaCorrente) {
            Double novoLimite = lerDouble("Digite o novo limite do cheque especial (R$):", "Alterar Limite");
            if (novoLimite == null) return;
            if (novoLimite < 0) {
                JOptionPane.showMessageDialog(null, "Erro: O limite não pode ser negativo.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ((ContaCorrente) conta).setLimiteChequeEspecial(novoLimite);
            JOptionPane.showMessageDialog(null, String.format("Limite do Cheque Especial atualizado para R$ %.2f!", novoLimite), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Erro: Esta conta não é do tipo Corrente!", "Operação Negada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void alterarLimiteDiarioMenu(BancoService service) {
        String num = lerString("Digite o número da conta:", "Alterar Limite Diário de Saque");
        if (num == null || num.isEmpty()) return;

        ContaBancaria conta = service.buscarConta(num);
        if (conta == null) {
            JOptionPane.showMessageDialog(null, "Erro: Conta não encontrada!", "Erro de Operação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double novoLimite = lerDouble("Digite o novo limite diário de saque (R$):", "Alterar Limite Diário");
        if (novoLimite == null) return;
        if (novoLimite < 0) {
            JOptionPane.showMessageDialog(null, "Erro: O limite diário não pode ser negativo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        conta.setLimiteDiarioSaque(novoLimite);
        JOptionPane.showMessageDialog(null, String.format("Limite diário de saque da conta %s atualizado para R$ %.2f!", num, novoLimite), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void executarRoteiroTestes(BancoService service) {
        JOptionPane.showMessageDialog(null, 
            "<html><body style='width: 320px; font-family: Arial, sans-serif;'>" +
            "<h3 style='color: #2e6da4;'>Iniciando Roteiro de Testes Obrigatório</h3>" +
            "O sistema executará automaticamente o fluxo completo de testes " +
            "exigido pelo professor e exibirá as caixas de diálogo passo a passo.<br/><br/>" +
            "<b>Fluxo a ser executado:</b><br/>" +
            "1. Cadastrar 2 Contas Correntes e 2 Poupança adicionais.<br/>" +
            "2. Realizar depósitos de R$ 500,00 nelas.<br/>" +
            "3. Sacar com saldo suficiente e ativar cheque especial.<br/>" +
            "4. Bloquear saque acima do limite do cheque especial.<br/>" +
            "5. Exibir extratos das contas criadas.<br/>" +
            "6. Exibir o histórico de transações.<br/>" +
            "7. Listar todas as contas cadastradas.<br/>" +
            "8. Exibir o relatório consolidado do banco.<br/>" +
            "9. Testar o novo Limite Diário de Saque (Bloqueio e Ajuste).<br/><br/>" +
            "Clique em <b>OK</b> para iniciar." +
            "</body></html>", 
            "Teste Automático", JOptionPane.INFORMATION_MESSAGE);

        // 1. Cadastrar pelo menos 2 CC e 2 CP
        Cliente t1 = new Cliente("Aluno Teste CC1", "111.111.111-11", "(11) 91111-1111");
        Cliente t2 = new Cliente("Aluno Teste CC2", "222.222.222-22", "(11) 92222-2222");
        Cliente t3 = new Cliente("Aluno Teste CP1", "333.333.333-33", "(11) 93333-3333");
        Cliente t4 = new Cliente("Aluno Teste CP2", "444.444.444-44", "(11) 94444-4444");

        ContaCorrente cc1 = new ContaCorrente("8801-C", t1, 0, 200.00); 
        ContaCorrente cc2 = new ContaCorrente("8802-C", t2, 0, 500.00);
        ContaPoupanca cp1 = new ContaPoupanca("8801-P", t3, 0, 0.005);
        ContaPoupanca cp2 = new ContaPoupanca("8802-P", t4, 0, 0.01);

        JOptionPane.showMessageDialog(null, "Passo 1: Cadastrando as 4 contas de teste (8801-C, 8802-C, 8801-P, 8802-P)...", "Passo 1 - Cadastro", JOptionPane.INFORMATION_MESSAGE);
        service.cadastrarContaSilencioso(cc1);
        service.cadastrarContaSilencioso(cc2);
        service.cadastrarContaSilencioso(cp1);
        service.cadastrarContaSilencioso(cp2);
        JOptionPane.showMessageDialog(null, "Contas de teste cadastradas com sucesso!", "Passo 1 - Concluído", JOptionPane.INFORMATION_MESSAGE);

        // 2. Realizar depósitos em todas as contas cadastradas
        JOptionPane.showMessageDialog(null, "Passo 2: Realizando depósitos de R$ 500,00 em todas as 4 contas de teste...", "Passo 2 - Depósitos", JOptionPane.INFORMATION_MESSAGE);
        cc1.depositar(500.0);
        cc2.depositar(500.0);
        cp1.depositar(500.0);
        cp2.depositar(500.0);

        // 3. Realizar saques com saldo suficiente e tentar saques que ativem o cheque especial em uma Conta Corrente
        JOptionPane.showMessageDialog(null, 
            "Passo 3: Testando saques na Conta Corrente 8801-C (Saldo: R$ 500,00 | Limite Cheque Especial: R$ 200,00).\n\n" +
            "- Primeiro saque: R$ 300,00 (Deve suceder com saldo próprio).\n" +
            "- Segundo saque: R$ 350,00 (Deve ultrapassar o saldo de R$ 200,00, ativando R$ 150,00 do Cheque Especial).", 
            "Passo 3 - Saques e Cheque Especial", JOptionPane.INFORMATION_MESSAGE);
        cc1.sacar(300.0); 
        cc1.sacar(350.0); 

        // 4. Tentar saque em Conta Corrente que ultrapasse o saldo mais o limite do cheque especial (deve ser bloqueado)
        JOptionPane.showMessageDialog(null, 
            "Passo 4: Tentando realizar saque de R$ 100,00 na 8801-C.\n" +
            "Saldo Atual: R$ -150,00 | Limite do Cheque Especial: R$ 200,00.\n" +
            "Disponível para saque: R$ 50,00.\n" +
            "O saque de R$ 100,00 DEVE SER BLOQUEADO por saldo insuficiente.", 
            "Passo 4 - Bloqueio de Saque Extralimite", JOptionPane.INFORMATION_MESSAGE);
        cc1.sacar(100.0); 

        // 5. Exibir o extrato completo de pelo menos uma conta de cada tipo
        JOptionPane.showMessageDialog(null, "Passo 5: Exibindo extrato da Conta Corrente 8801-C e da Conta Poupança 8801-P...", "Passo 5 - Extratos", JOptionPane.INFORMATION_MESSAGE);
        cc1.gerarExtrato();
        cp1.gerarExtrato();

        // 6. Exibir o histórico de transações de uma conta
        JOptionPane.showMessageDialog(null, "Passo 6: Exibindo histórico detalhado da Conta Corrente 8801-C...", "Passo 6 - Histórico", JOptionPane.INFORMATION_MESSAGE);
        cc1.exibirHistorico();

        // 7. Listar todas as contas cadastradas
        JOptionPane.showMessageDialog(null, "Passo 7: Listando todas as contas do banco no sistema...", "Passo 7 - Listagem", JOptionPane.INFORMATION_MESSAGE);
        service.listarTodasAsContas();

        // 8. Exibir o relatório geral do banco
        JOptionPane.showMessageDialog(null, "Passo 8: Exibindo relatório consolidadado de gestão do banco...", "Passo 8 - Relatório Geral", JOptionPane.INFORMATION_MESSAGE);
        service.exibirRelatorioGeral();

        // 9. Testar o novo Limite Diário de Saque (Passo Adicional do Senior Dev)
        JOptionPane.showMessageDialog(null, 
            "Passo 9: Testando o Limite Diário de Saque na Conta Poupança 8801-P.\n\n" +
            "Saldo da Conta: R$ 500,00.\n" +
            "Limite Diário de Saque Atual (Padrão): R$ 2.500,00.\n" +
            "Ajustando o limite diário da conta para R$ 200,00.\n" +
            "Tentaremos sacar R$ 300,00. O saque deve ser bloqueado por limite diário excedido (mesmo com R$ 500,00 de saldo).", 
            "Passo 9 - Limite Diário de Saque", JOptionPane.INFORMATION_MESSAGE);
        
        cp1.setLimiteDiarioSaque(200.0);
        
        // Saque deve falhar devido ao limite diário (R$ 300 > R$ 200)
        boolean sucessoSaqueLimite = cp1.sacar(300.0);
        
        JOptionPane.showMessageDialog(null, 
            "Agora ajustaremos o limite diário da 8801-P para R$ 600,00 e tentaremos sacar R$ 300,00 novamente. O saque deve ser efetuado com sucesso.", 
            "Passo 9 - Ajustando Limite Diário", JOptionPane.INFORMATION_MESSAGE);
        
        cp1.setLimiteDiarioSaque(600.0);
        boolean sucessoSaqueLimiteOk = cp1.sacar(300.0);
        
        if (sucessoSaqueLimiteOk) {
            JOptionPane.showMessageDialog(null, 
                "Saque efetuado com sucesso! Saldo atual da conta: R$ 200,00.\n" +
                "Valor sacado hoje: R$ 300,00. Limite diário: R$ 600,00.\n\n" +
                "Tentaremos agora outro saque de R$ 310,00.\n" +
                "Embora o saldo disponível (R$ 200,00) não baste, o limite diário de saque (R$ 300,00 já sacados + R$ 310,00 solicitado = R$ 610,00 > R$ 600,00) também seria excedido.\n" +
                "O limite diário irá barrar o saque por exceder os R$ 600,00 diários.", 
                "Passo 9 - Teste Acumulado", JOptionPane.INFORMATION_MESSAGE);
            cp1.sacar(310.0);
        }

        JOptionPane.showMessageDialog(null, "Roteiro de Testes Concluído com Sucesso!", "Fim do Teste", JOptionPane.INFORMATION_MESSAGE);
     }

    private static String lerString(String mensagem, String titulo) {
        String input = JOptionPane.showInputDialog(null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE);
        if (input == null) {
            return null;
        }
        return input.trim();
    }

    private static Double lerDouble(String mensagem, String titulo) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                return null;
            }
            try {
                return Double.parseDouble(input.replace(',', '.').trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um valor numérico válido (ex: 1250.50).", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static Integer lerInteiro(String mensagem, String titulo) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                return null;
            }
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, insira uma opção numérica válida.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
