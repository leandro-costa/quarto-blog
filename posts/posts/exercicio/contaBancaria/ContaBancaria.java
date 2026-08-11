package contaBancaria;

public class ContaBancaria {
    private String numeroConta;
    private String titular;
    private double saldo;
    private double limite;
    private double ultimaOperacao;
    private String tipoUltimaOperacao;

    public ContaBancaria(String numeroConta, String titular) {
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = 0;
        this.limite = 100.0;
        this.ultimaOperacao = 0;
        this.tipoUltimaOperacao = "Nenhuma";
    }

    public ContaBancaria(String numeroConta, String titular, double limite) {
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = 0;
        this.limite = limite;
        this.ultimaOperacao = 0;
        this.tipoUltimaOperacao = "Nenhuma";
    }

    public boolean depositar(double valor) {
        if (valor <= 0) {
            System.out.println("Erro: O valor do depósito deve ser maior que zero.");
            return false;
        }
        this.saldo += valor;
        this.ultimaOperacao = valor;
        this.tipoUltimaOperacao = "Depósito";
        System.out.println("Depósito de R$ " + valor + " realizado com sucesso.");
        return true;
    }

    public boolean sacar(double valor) {
        if (valor <= 0) {
            System.out.println("Erro: O valor do saque deve ser maior que zero.");
            return false;
        }
        double saldoTotal = this.saldo + this.limite;
        if (valor > saldoTotal) {
            System.out.println("Erro: Saldo insuficiente para realizar o saque.");
            return false;
        }
        this.saldo -= valor;
        this.ultimaOperacao = valor;
        this.tipoUltimaOperacao = "Saque";
        System.out.println("Saque de R$ " + valor + " realizado com sucesso.");
        return true;
    }

    public boolean transferir(ContaBancaria destino, double valor) {
        if (valor <= 0) {
            System.out.println("Erro: O valor da transferência deve ser maior que zero.");
            return false;
        }
        double saldoTotal = this.saldo + this.limite;
        if (valor > saldoTotal) {
            System.out.println("Erro: Saldo insuficiente para realizar a transferência.");
            return false;
        }
        if (destino == null) {
            System.out.println("Erro: Conta de destino inválida.");
            return false;
        }
        this.saldo -= valor;
        destino.saldo += valor;
        this.ultimaOperacao = valor;
        this.tipoUltimaOperacao = "Transferência Enviada";
        destino.ultimaOperacao = valor;
        destino.tipoUltimaOperacao = "Transferência Recebida";
        System.out.println("Transferência de R$ " + valor + " para conta " + destino.numeroConta + " realizada com sucesso.");
        return true;
    }

    public double getSaldo() {
        return this.saldo;
    }

    public double getLimite() {
        return this.limite;
    }

    public double getSaldoTotal() {
        return this.saldo + this.limite;
    }

    public String getNumeroConta() {
        return this.numeroConta;
    }

    public String getTitular() {
        return this.titular;
    }

    public void exibirInformacoes() {
        System.out.println("=== Informações da Conta ===");
        System.out.println("Número da Conta: " + this.numeroConta);
        System.out.println("Titular: " + this.titular);
        System.out.println("Saldo: R$ " + this.saldo);
        System.out.println("Limite: R$ " + this.limite);
        System.out.println("Saldo Total Disponível: R$ " + getSaldoTotal());
        System.out.println("Última Operação: " + this.tipoUltimaOperacao + " - R$ " + this.ultimaOperacao);
        System.out.println();
    }
}