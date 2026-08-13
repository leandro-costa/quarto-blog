import java.util.List;

public class CalculadoraArea {

    // O método aceita uma lista de QUALQUER tipo, desde que implemente FormaGeometrica
    public double calcularAreaTotal(List<? extends FormaGeometrica> formas) {
        return formas.stream()
                .mapToDouble(FormaGeometrica::calcularArea)
                .sum();
    }
}
