import java.util.List;

public class TestaCalculadoraArea {
    public static void main(String[] args) {
        // Criando formas geométricas variadas
        Circulo circulo = new Circulo(5.0);
        Retangulo retangulo = new Retangulo(4.0, 6.0);

        // Criando uma lista que aceita qualquer FormaGeometrica
        List<FormaGeometrica> listaDeFormas = new ArrayList<>();
        listaDeFormas.add(circulo);
        listaDeFormas.add(retangulo);

        // Calculadora processa a lista mista
        CalculadoraArea calculadora = new CalculadoraArea();
        double areaTotal = calculadora.calcularAreaTotal(listaDeFormas);

        System.out.println("Área Total (Círculo + Retângulo): " + areaTotal);
    }
}
