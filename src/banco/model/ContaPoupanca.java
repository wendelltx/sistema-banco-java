package banco.model;

import javax.swing.JOptionPane;

public class ContaPoupanca extends ContaBancaria {
    private double taxaRendimentoMensal;

    public ContaPoupanca(String numeroConta, Cliente titular, double saldo, double taxaRendimentoMensal) {
        super(numeroConta, titular, saldo);
        this.taxaRendimentoMensal = taxaRendimentoMensal;
    }

    public double calcularRendimento() {
        return getSaldo() * taxaRendimentoMensal;
    }

    public void aplicarRendimento() {
        double rendimento = calcularRendimento();
        setSaldo(getSaldo() + rendimento);
        registrarTransacao(String.format("RENDIMENTO APLICADO: R$ %.2f (Taxa: %.2f%%)", rendimento, taxaRendimentoMensal * 100));
        JOptionPane.showMessageDialog(null, String.format("Rendimento de R$ %.2f aplicado com sucesso!", rendimento), "Rendimento Aplicado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void gerarExtrato() {
        StringBuilder dadosExtrato = new StringBuilder();
        double rendimentoEstimado = calcularRendimento();
        double disponivelDiario = getLimiteDiarioSaque() - getValorSacadoHoje();

        dadosExtrato.append("=============================\n")
                    .append("      EXTRATO CONTA POUPANÇA \n")
                    .append("=============================\n")
                    .append("Conta N°: ").append(getNumeroConta()).append("\n")
                    .append("Titular: ").append(getTitular().getNome()).append("\n")
                    .append("CPF: ").append(getTitular().getCpf()).append("\n")
                    .append("-----------------------------\n")
                    .append(String.format("Saldo Atual: R$ %.2f\n", getSaldo()))
                    .append(String.format("Taxa Rendimento Mensal: %.2f%%\n", taxaRendimentoMensal * 100))
                    .append(String.format("Rendimento Estimado Próx. Mês: R$ %.2f\n", rendimentoEstimado))
                    .append("-----------------------------\n")
                    .append(String.format("Limite Diário de Saque: R$ %.2f\n", getLimiteDiarioSaque()))
                    .append(String.format("Já Sacado Hoje: R$ %.2f\n", getValorSacadoHoje()))
                    .append(String.format("Limite Diário Restante: R$ %.2f\n", disponivelDiario))
                    .append("-----------------------------\n")
                    .append("HISTÓRICO DE TRANSAÇÕES:\n");

        if (getHistorico().isEmpty()) {
            dadosExtrato.append("Nenhuma transação realizada.\n");
        } else {
            for (String transacao : getHistorico()) {
                dadosExtrato.append(transacao).append("\n");
            }
        }
        dadosExtrato.append("=============================");

        JOptionPane.showMessageDialog(null, dadosExtrato.toString(), "Extrato Detalhado", JOptionPane.INFORMATION_MESSAGE);
    }

    public double getTaxaRendimentoMensal() {
        return taxaRendimentoMensal;
    }

    public void setTaxaRendimentoMensal(double taxaRendimentoMensal) {
        this.taxaRendimentoMensal = taxaRendimentoMensal;
    }
}
