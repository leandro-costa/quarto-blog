public abstract class Veiculo<T extends Motor> {
    private String modelo;
    private T motor;

    public Veiculo(String modelo, T motor) {
        this.modelo = modelo;
        this.motor = motor;
    }

    public String getModelo() {
        return modelo;
    }

    public T getMotor() {
        return motor;
    }

    public abstract void ligar();
}
