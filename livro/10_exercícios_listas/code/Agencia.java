
import java.util.ArrayList;
import java.util.List;

public class Agencia {
    private int numero;
    private ArrayList<Conta> contas;

    public Agencia(int numero) {
        this.numero = numero;
        contas = new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public int criarConta(String cliente) {
        // calcula o numero da nova conta
        int numeroConta = numero * 100;
        numeroConta += contas.size();
        // instancia nova conta com o numero calculado
        Conta novConta = new Conta(this,numeroConta, cliente);
        // guardo nova conta na minha lista de contas
        contas.add(novConta);
        // devolvo a conta para quem pediu
        return novConta.getNumero();
    }

    public int totalContas() {
        return contas.size();
    }

    public double totalDinheiro() {
        double total = 0;
        for (Conta conta : contas) {
            total += conta.getSaldo();
        }
        return total;
    }

    public Conta getConta(int numeroConta) {
        // buscar a conta que tem o numero igual a numeroConta
        for (Conta conta : contas) {
            if (conta.getNumero() == numeroConta) {
                return conta;
            }
        }
        return null;
    }

    public List<Conta> getContas() {
        return List.copyOf(contas);
    }

}
