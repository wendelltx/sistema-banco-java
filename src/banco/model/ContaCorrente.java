package banco.model;

import javax.swing.JOptionPane;

public class ContaCorrente extends ContaBancaria {
    private double limiteChequeEspecial;

    public ContaCorrente(String numeroConta, Cliente titular, double saldoAtual, double limiteChequeEspecial) {
        super(numeroConta, titular, saldoAtual);
        this.limiteChequeEspecial = limiteChequeEspecial;
    }

    @Override
    public boolean sacar(double valor) {
        if (valor <= 0) {
            JOptionPane.showMessageDialog(null, "Erro: O valor do saque deve ser maior que zero.", "Erro de Operação",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!verificarLimiteDiario(valor)) {
            return false;
        }

        double saldoAtual = getSaldo();

        if (valor <= saldoAtual) {
            setSaldo(saldoAtual - valor);
            registrarTransacao(String.format("SAQUE (CONTA CORRENTE): R$ %.2f", valor));
            registrarSaqueDiario(valor);
            JOptionPane.showMessageDialog(null, "Saque realizado com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else if (valor <= (saldoAtual + this.limiteChequeEspecial)) {
            usarChequeEspecial(valor);
            registrarSaqueDiario(valor);
            return true;
        }

        JOptionPane.showMessageDialog(null, "Erro: Saldo e limite de Cheque Especial insuficientes.",
                "Saldo Insuficiente", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public void usarChequeEspecial(double valor) {
        double saldoAnterior = getSaldo();
        setSaldo(saldoAnterior - valor);
        registrarTransacao(String.format("USO DE CHEQUE ESPECIAL: R$ %.2f (Saldo anterior: R$ %.2f)", valor, saldoAnterior));
        JOptionPane.showMessageDialog(null, "Saque realizado utilizando o Cheque Especial!", "Aviso",
                JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void gerarExtrato() {
        StringBuilder dadosExtrato = new StringBuilder();
        double disponivelDiario = getLimiteDiarioSaque() - getValorSacadoHoje();
        dadosExtrato.append("=============================\n")
                    .append("      EXTRATO CONTA CORRENTE \n")
                    .append("=============================\n")
                    .append("Conta N°: ").append(getNumeroConta()).append("\n")
                    .append("Titular: ").append(getTitular().getNome()).append("\n")
                    .append("CPF: ").append(getTitular().getCpf()).append("\n")
                    .append("-----------------------------\n")
                    .append(String.format("Saldo em Conta: R$ %.2f\n", getSaldo()))
                    .append(String.format("Limite Cheque Especial: R$ %.2f\n", this.limiteChequeEspecial))
                    .append(String.format("Total Disponível para Saque: R$ %.2f\n", (getSaldo() + this.limiteChequeEspecial)))
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

    public double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    public void setLimiteChequeEspecial(double limiteChequeEspecial) {
        this.limiteChequeEspecial = limiteChequeEspecial;
    }
}
