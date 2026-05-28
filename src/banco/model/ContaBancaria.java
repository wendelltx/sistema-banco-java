package banco.model;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import banco.interfaces.Operavel;
import javax.swing.JOptionPane;

public abstract class ContaBancaria implements Operavel {
    private String numeroConta;
    private Cliente titular;
    private double saldo;
    private List<String> historico;
    private double limiteDiarioSaque;
    private double valorSacadoHoje;
    private java.time.LocalDate dataUltimoSaque;

    public ContaBancaria(String numeroConta, Cliente titular, double saldo) {
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = saldo;
        this.historico = new ArrayList<>();
        this.limiteDiarioSaque = 2500.0;
        this.valorSacadoHoje = 0.0;
        this.dataUltimoSaque = java.time.LocalDate.now();
        if (saldo > 0) {
            registrarTransacao(String.format("DEPÓSITO INICIAL: R$ %.2f", saldo));
        }
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public Cliente getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public List<String> getHistorico() {
        return historico;
    }

    public void setHistorico(List<String> historico) {
        this.historico = historico;
    }

    public void depositar(double valor) {
        if (valor <= 0) {
            JOptionPane.showMessageDialog(null, "Erro: O valor do depósito deve ser maior que zero.",
                    "Erro de Operação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.saldo += valor;
        registrarTransacao(String.format("DEPÓSITO: R$ %.2f", valor));
        JOptionPane.showMessageDialog(null, "Depósito realizado com sucesso!", "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean sacar(double valor) {
        if (valor <= 0) {
            JOptionPane.showMessageDialog(null, "Erro: O valor do saque deve ser maior que zero.", "Erro de Operação",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!verificarLimiteDiario(valor)) {
            return false;
        }
        if (valor <= this.saldo) {
            this.saldo -= valor;
            registrarTransacao(String.format("SAQUE: R$ %.2f", valor));
            registrarSaqueDiario(valor);
            return true;
        }
        JOptionPane.showMessageDialog(null, "Erro: Saldo insuficiente.", "Saldo Insuficiente",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public void exibirSaldo() {
        java.time.LocalDate hoje = java.time.LocalDate.now();
        if (this.dataUltimoSaque == null || !this.dataUltimoSaque.equals(hoje)) {
            this.valorSacadoHoje = 0.0;
            this.dataUltimoSaque = hoje;
        }
        double disponivelHoje = this.limiteDiarioSaque - this.valorSacadoHoje;
        String mensagem = String.format("Conta: %s\nTitular: %s\nSaldo Atual: R$ %.2f\nLimite Diário de Saque: R$ %.2f\nDisponível para Saque Hoje: R$ %.2f",
                this.numeroConta, this.titular.getNome(), this.saldo, this.limiteDiarioSaque, disponivelHoje);
        JOptionPane.showMessageDialog(null, mensagem, "Saldo Disponível", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void registrarTransacao(String descricao) {
        LocalDateTime horario = LocalDateTime.now();
        DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHoraFormatada = horario.format(formatacao);
        String linhaDoHistorico = String.format("[%s] %s", dataHoraFormatada, descricao);
        this.historico.add(linhaDoHistorico);
    }

    public void exibirHistorico() {
        if (this.historico.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma transação realizada nesta conta.", "Histórico",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=== HISTÓRICO DA CONTA ").append(this.numeroConta).append(" ===\n");
        for (String transacao : this.historico) {
            sb.append(transacao).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Histórico de Transações", JOptionPane.INFORMATION_MESSAGE);
    }

    public String mostrarResumo() {
        return String.format("Conta N°: %s | Titular: %s | Saldo: R$ %.2f", 
                this.numeroConta, this.titular.getNome(), this.saldo);
    }

    public boolean verificarLimiteDiario(double valor) {
        java.time.LocalDate hoje = java.time.LocalDate.now();
        if (this.dataUltimoSaque == null || !this.dataUltimoSaque.equals(hoje)) {
            this.valorSacadoHoje = 0.0;
            this.dataUltimoSaque = hoje;
        }
        if (this.valorSacadoHoje + valor > this.limiteDiarioSaque) {
            JOptionPane.showMessageDialog(null, 
                String.format("Erro: Limite diário de saque excedido!\n" +
                              "Limite Diário: R$ %.2f\n" +
                              "Já sacado hoje: R$ %.2f\n" +
                              "Valor solicitado: R$ %.2f\n" +
                              "Disponível hoje: R$ %.2f", 
                              this.limiteDiarioSaque, this.valorSacadoHoje, valor, (this.limiteDiarioSaque - this.valorSacadoHoje)), 
                "Limite Diário Excedido", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void registrarSaqueDiario(double valor) {
        this.valorSacadoHoje += valor;
        this.dataUltimoSaque = java.time.LocalDate.now();
    }

    public double getLimiteDiarioSaque() {
        return limiteDiarioSaque;
    }

    public void setLimiteDiarioSaque(double limiteDiarioSaque) {
        this.limiteDiarioSaque = limiteDiarioSaque;
    }

    public double getValorSacadoHoje() {
        return valorSacadoHoje;
    }

    public void setValorSacadoHoje(double valorSacadoHoje) {
        this.valorSacadoHoje = valorSacadoHoje;
    }

    public java.time.LocalDate getDataUltimoSaque() {
        return dataUltimoSaque;
    }

    public void setDataUltimoSaque(java.time.LocalDate dataUltimoSaque) {
        this.dataUltimoSaque = dataUltimoSaque;
    }

    public abstract void gerarExtrato();
}