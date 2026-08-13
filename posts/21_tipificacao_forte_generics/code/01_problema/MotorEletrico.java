public class MotorEletrico extends Motor {
    private int potenciaKW;

    public MotorEletrico(int potenciaKW) {
        super("Elétrico");
        this.potenciaKW = potenciaKW;
    }

    public int getPotenciaKW() {
        return potenciaKW;
    }
}
