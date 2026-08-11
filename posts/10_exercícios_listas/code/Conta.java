
public class Conta {
    private int numero;
    private String cliente;
    private double saldo;
    private double limite;
    private Agencia agencia;

    public Conta(Agencia agencia,int numero, String cliente) {
        this(agencia,numero, cliente, 0, 100);
    }

    public Conta(Agencia agencia,int numero, String cliente, double saldo, double limite) {
        this.agencia=agencia;
        if (numero < 0) {
            this.numero = 999;
        } else {
            this.numero = numero;
        }
        setCliente(cliente);
        this.saldo = saldo;
        this.limite = limite;
    }

    public void setCliente(String cliente) {
        if (cliente != null && !cliente.isEmpty() && !cliente.isBlank()) {
            this.cliente = cliente;
        }else{
            this.cliente = "GERENTE";
        }
    }

    public boolean saca(double quantidade) {// método
        if (this.saldo < quantidade) {
            return false;
        } else {
            this.saldo -= quantidade;
            return true;
        }
    }

    public void deposita(double quantidade) {// método
        this.saldo += quantidade;
    }

    public boolean transferir(Conta destino, double valor) {
        if (this.saca(valor)) {
            destino.deposita(valor);
            return true;
        }
        return false;
    }

    public int getNumero() {
        return numero;
    }
    public double getLimite() {
        return limite;
    }
    public String getCliente() {
        return cliente;
    }
    public double getSaldo() {
        return saldo;
    }
    public Agencia getAgencia() {
        return agencia;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Conta outraConta){
            return numero == outraConta.getNumero();
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Conta [numero= %d, cliente= %s, saldo= %.2f]", numero, cliente, saldo);
    }

}
