// Erro de compilação: Integer não herda de Motor!
public class Pop extends Veiculo<Integer> {
    public Pop(String modelo, Integer motor) {
        super(modelo, motor);
    }

    @Override
    public void ligar() {
        IO.println("Pop " + getModelo() + " está ligada.");
    }
}
