public class Carro extends Veiculo<MotorCombustao> {
    public Carro(String modelo, MotorCombustao motor) {
        super(modelo, motor);
    }

    @Override
    public void ligar() {
        IO.println("Carro " + getModelo() + " está ligado.");
    }
}
