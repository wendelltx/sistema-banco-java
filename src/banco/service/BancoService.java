package banco.service;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import banco.model.ContaBancaria;
import banco.model.ContaCorrente;
import banco.model.ContaPoupanca;

public class BancoService {
    private List<ContaCorrente> contasCorrentes = new ArrayList<>();
    private List<ContaPoupanca> contasPoupanca = new ArrayList<>();

    public void cadastrarContaCorrente(ContaCorrente cc) {
        if (buscarConta(cc.getNumeroConta()) != null) {
            JOptionPane.showMessageDialog(null, 
                "Erro: Já existe uma conta cadastrada com o número " + cc.getNumeroConta() + "!", 
                "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        contasCorrentes.add(cc);
        JOptionPane.showMessageDialog(null, 
            "Conta Corrente N° " + cc.getNumeroConta() + " cadastrada com sucesso!", 
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cadastrarContaPoupanca(ContaPoupanca cp) {
        if (buscarConta(cp.getNumeroConta()) != null) {
            JOptionPane.showMessageDialog(null, 
                "Erro: Já existe uma conta cadastrada com o número " + cp.getNumeroConta() + "!", 
                "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        contasPoupanca.add(cp);
        JOptionPane.showMessageDialog(null, 
            "Conta Poupança N° " + cp.getNumeroConta() + " cadastrada com sucesso!", 
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Cadastro silencioso usado apenas para pré-população de dados de teste,
     * evitando caixas de diálogo repetitivas na inicialização do sistema.
     */
    public void cadastrarContaSilencioso(ContaBancaria conta) {
        if (buscarConta(conta.getNumeroConta()) != null) {
            return;
        }
        if (conta instanceof ContaCorrente) {
            contasCorrentes.add((ContaCorrente) conta);
        } else if (conta instanceof ContaPoupanca) {
            contasPoupanca.add((ContaPoupanca) conta);
        }
    }

    public ContaBancaria buscarConta(String numeroConta) {
        for (ContaCorrente cc : contasCorrentes) {
            if (cc.getNumeroConta().equals(numeroConta)) {
                return cc;
            }
        }
        for (ContaPoupanca cp : contasPoupanca) {
            if (cp.getNumeroConta().equals(numeroConta)) {
                return cp;
            }
        }
        return null;
    }

    public void listarTodasAsContas() {
        if (contasCorrentes.isEmpty() && contasPoupanca.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Nenhuma conta cadastrada no sistema.", 
                "Lista de Contas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: Arial, sans-serif; width: 350px;'>");
        sb.append("<h2 style='color: #2e6da4; border-bottom: 2px solid #2e6da4; padding-bottom: 5px; margin-top: 0;'>Lista de Contas Cadastradas</h2>");
        
        sb.append("<h4 style='color: #2a6496; margin-bottom: 5px; margin-top: 10px;'>Contas Correntes</h4>");
        if (contasCorrentes.isEmpty()) {
            sb.append("<p style='color: #777; font-style: italic; margin-top: 2px;'>Nenhuma conta corrente cadastrada.</p>");
        } else {
            sb.append("<table border='0' cellspacing='3' cellpadding='2' style='width: 100%;'>");
            for (ContaCorrente cc : contasCorrentes) {
                sb.append("<tr>")
                  .append("<td style='font-weight: bold; width: 90px;'>N° ").append(cc.getNumeroConta()).append("</td>")
                  .append("<td>").append(cc.getTitular().getNome()).append("</td>")
                  .append("<td style='text-align: right; font-weight: bold; color: #4cae4c;'>R$ ").append(String.format("%.2f", cc.getSaldo())).append("</td>")
                  .append("</tr>");
            }
            sb.append("</table>");
        }

        sb.append("<h4 style='color: #2a6496; margin-bottom: 5px; margin-top: 15px;'>Contas Poupança</h4>");
        if (contasPoupanca.isEmpty()) {
            sb.append("<p style='color: #777; font-style: italic; margin-top: 2px;'>Nenhuma conta poupança cadastrada.</p>");
        } else {
            sb.append("<table border='0' cellspacing='3' cellpadding='2' style='width: 100%;'>");
            for (ContaPoupanca cp : contasPoupanca) {
                sb.append("<tr>")
                  .append("<td style='font-weight: bold; width: 90px;'>N° ").append(cp.getNumeroConta()).append("</td>")
                  .append("<td>").append(cp.getTitular().getNome()).append("</td>")
                  .append("<td style='text-align: right; font-weight: bold; color: #4cae4c;'>R$ ").append(String.format("%.2f", cp.getSaldo())).append("</td>")
                  .append("</tr>");
            }
            sb.append("</table>");
        }
        sb.append("</body></html>");

        JOptionPane.showMessageDialog(null, sb.toString(), "Contas Cadastradas", JOptionPane.INFORMATION_MESSAGE);
    }

    public double calcularPatrimonioTotal() {
        double total = 0;
        for (ContaCorrente cc : contasCorrentes) {
            total += cc.getSaldo();
        }
        for (ContaPoupanca cp : contasPoupanca) {
            total += cp.getSaldo();
        }
        return total;
    }

    public void exibirRelatorioGeral() {
        int totalCC = contasCorrentes.size();
        int totalCP = contasPoupanca.size();
        int totalContas = totalCC + totalCP;
        double patrimonioTotal = calcularPatrimonioTotal();

        ContaBancaria maior = null;
        ContaBancaria menor = null;

        // Encontrar maior e menor saldo
        for (ContaCorrente cc : contasCorrentes) {
            if (maior == null || cc.getSaldo() > maior.getSaldo()) {
                maior = cc;
            }
            if (menor == null || cc.getSaldo() < menor.getSaldo()) {
                menor = cc;
            }
        }

        for (ContaPoupanca cp : contasPoupanca) {
            if (maior == null || cp.getSaldo() > maior.getSaldo()) {
                maior = cp;
            }
            if (menor == null || cp.getSaldo() < menor.getSaldo()) {
                menor = cp;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: Arial, sans-serif; width: 320px;'>");
        sb.append("<h2 style='color: #2e6da4; border-bottom: 2px solid #2e6da4; padding-bottom: 5px; margin-top: 0;'>Relatório Geral do Banco</h2>");
        sb.append("<p style='margin-bottom: 5px;'><b>Resumo Quantitativo por Tipo:</b></p>");
        sb.append("<ul style='margin-top: 2px;'>");
        sb.append("<li>Contas Correntes: ").append(totalCC).append("</li>");
        sb.append("<li>Contas Poupança: ").append(totalCP).append("</li>");
        sb.append("<li><b>Total de Contas: </b>").append(totalContas).append("</li>");
        sb.append("</ul>");
        
        sb.append("<p style='margin-bottom: 5px;'><b>Patrimônio Total Custodiado:</b><br/>");
        sb.append("<span style='font-size: 15px; color: #4cae4c; font-weight: bold;'>R$ ").append(String.format("%.2f", patrimonioTotal)).append("</span></p>");

        sb.append("<p style='margin-bottom: 5px;'><b>Destaques de Saldo:</b></p>");
        
        if (maior != null) {
            sb.append("<span style='color: #4cae4c; font-weight: bold;'>▲ Maior Saldo:</span> Conta N° ").append(maior.getNumeroConta())
              .append(" (").append(maior.getTitular().getNome()).append(")<br/>")
              .append("<span style='font-weight: bold; margin-left: 15px;'>R$ ").append(String.format("%.2f", maior.getSaldo())).append("</span><br/>");
        } else {
            sb.append("<span style='color: #4cae4c; font-weight: bold;'>▲ Maior Saldo:</span> N/A<br/>");
        }

        sb.append("<div style='height: 8px;'></div>");

        if (menor != null) {
            sb.append("<span style='color: #d9534f; font-weight: bold;'>▼ Menor Saldo:</span> Conta N° ").append(menor.getNumeroConta())
              .append(" (").append(menor.getTitular().getNome()).append(")<br/>")
              .append("<span style='font-weight: bold; margin-left: 15px;'>R$ ").append(String.format("%.2f", menor.getSaldo())).append("</span>");
        } else {
            sb.append("<span style='color: #d9534f; font-weight: bold;'>▼ Menor Saldo:</span> N/A");
        }
        sb.append("</body></html>");

        JOptionPane.showMessageDialog(null, sb.toString(), "Relatório de Gestão Bancária", JOptionPane.INFORMATION_MESSAGE);
    }
}
