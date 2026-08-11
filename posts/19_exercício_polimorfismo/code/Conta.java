class Conta{
    private double saldo;

    public Conta() {
        saldo = 0;
    }

    public void executarOperacao(Operacao opr){
        saldo+=opr.operar();
    }

    public double getSaldo() {
        return saldo;
    }
}