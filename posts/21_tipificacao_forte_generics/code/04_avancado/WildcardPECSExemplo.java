import java.util.ArrayList;
import java.util.List;

public class WildcardPECSExemplo {

    // PRODUCER EXTENDS: apenas leitura da lista
    public static double somarAreas(List<? extends FormaGeometrica> formas) {
        double total = 0;
        for (FormaGeometrica f : formas) {
            total += f.calcularArea();
        }
        return total;
    }

    // CONSUMER SUPER: apenas adição/escrita na lista
    public static void adicionarCirculo(List<? super Circulo> lista, double raio) {
        lista.add(new Circulo(raio));
    }

    public static void main(String[] args) {
        List<Circulo> circulos = new ArrayList<>();
        circulos.add(new Circulo(2.0));
        circulos.add(new Circulo(3.0));

        // Producer Extends em ação: aceita List<Circulo> em parâmetro List<? extends FormaGeometrica>
        System.out.println("Soma das áreas: " + somarAreas(circulos));

        // Consumer Super em ação
        List<Object> objetos = new ArrayList<>();
        adicionarCirculo(objetos, 4.0);
        System.out.println("Círculo adicionado em lista genérica com sucesso.");
    }

    interface FormaGeometrica {
        double calcularArea();
    }

    static class Circulo implements FormaGeometrica {
        private double raio;
        public Circulo(double raio) { this.raio = raio; }
        @Override public double calcularArea() { return Math.PI * raio * raio; }
    }
}
