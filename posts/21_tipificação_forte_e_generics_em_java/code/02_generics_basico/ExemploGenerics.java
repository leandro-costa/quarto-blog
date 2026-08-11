import java.util.ArrayList;
import java.util.List;

public class ExemploGenerics {
    public static void main(String[] args) {
        // Lista com generics (tipo específico: String)
        List<String> listaDeStrings = new ArrayList<>();
        
        // Adicionando elementos (apenas Strings são permitidas)
        listaDeStrings.add("Texto 1");
        listaDeStrings.add("Texto 2");
        // listaDeStrings.add(10); // Erro de compilação: tipo incompatível

        // Recuperando elementos (sem necessidade de cast)
        String texto1 = listaDeStrings.get(0);
        String texto2 = listaDeStrings.get(1);

        System.out.println("Texto 1: " + texto1);
        System.out.println("Texto 2: " + texto2);

        // Exemplo com classe genérica
        Caixa<Integer> caixaDeInteiros = new Caixa<>();
        caixaDeInteiros.setConteudo(42);
        int valor = caixaDeInteiros.getConteudo(); // Sem cast
        System.out.println("Valor na caixa: " + valor);
    }
}
