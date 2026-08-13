import java.util.ArrayList;
import java.util.List;

public class ExemploObject {
    public static void main(String[] args) {
        // Lista sem generics (usa Object)
        List lista = new ArrayList();
        
        // Adicionando elementos de tipos diferentes
        lista.add("Texto"); // String
        lista.add(10);      // Integer
        lista.add(3.14);    // Double

        // Recuperando elementos (precisa de cast)
        String texto = (String) lista.get(0);
        Integer numero = (Integer) lista.get(1);
        Double decimal = (Double) lista.get(2);

        System.out.println("Texto: " + texto);
        System.out.println("Número: " + numero);
        System.out.println("Decimal: " + decimal);

        // Problema: Erro em tempo de execução se o cast estiver errado
        try {
            Integer erro = (Integer) lista.get(0); // ClassCastException
        } catch (ClassCastException e) {
            System.out.println("Erro de cast em runtime: " + e.getMessage());
        }
    }
}
