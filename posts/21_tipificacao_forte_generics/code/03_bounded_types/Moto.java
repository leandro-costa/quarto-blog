public class Moto extends Veiculo<MotorCombustao> {
    public Moto(String modelo, MotorCombustao motor) {
        super(modelo, motor);
    }

    @Override
    public void ligar() {
        IO.println("Moto " + getModelo() + " está ligada.");
    }
}
