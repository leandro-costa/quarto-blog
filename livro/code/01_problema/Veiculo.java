public abstract class Veiculo {
    private String modelo;
    private Motor motor;

    public Veiculo(String modelo, Motor motor) {
        this.modelo = modelo;
        this.motor = motor;
    }

    public String getModelo() {
        return modelo;
    }

    public Motor getMotor() {
        return motor;
    }

    public abstract void ligar();
}
