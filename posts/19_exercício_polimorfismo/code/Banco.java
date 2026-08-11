import java.util.ArrayList;
import java.util.List;

public class Banco {
    private List<Correntista> correntistas;


    public Banco() {
        this.correntistas = new ArrayList();
    }

    public void addCorrentista(String nome){
        correntistas.add(new Correntista(nome));
    }

    public Correntista getCorrentista(String nome) {
        for (Correntista correntista : correntistas) {
            if (correntista.getNome().equals(nome)) {
                return correntista;
            }
        }
        return null;
    }

    public void debitar(String nomeCorrentista, double valor) {
        Debito d = new Debito(valor);
        getCorrentista(nomeCorrentista).getConta().executarOperacao(d);
    }

    public void creditar(String nomeCorrentista, double valor) {
        Credito c = new Credito(valor);
        getCorrentista(nomeCorrentista).getConta().executarOperacao(c);
    }

    public double getSaldo(String nomeCorrentista) {
        return getCorrentista(nomeCorrentista).getConta().getSaldo();
    }

    public void transferir(String nomeCorrentistaOrigem, String nomeCorrentistaDestino, double valor) {
        debitar(nomeCorrentistaOrigem, valor);
        creditar(nomeCorrentistaDestino, valor);
    }
}