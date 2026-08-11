public class Correntista {
    private String nome;
    private Conta conta;

    public Correntista(String nome) {
        this.nome = nome;
        this.conta = new Conta();
    }

    public String getNome() {
        return nome;
    }

    public Conta getConta() {
        return conta;
    }
}
