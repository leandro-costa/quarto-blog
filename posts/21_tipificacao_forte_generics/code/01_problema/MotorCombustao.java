public class MotorCombustao extends Motor {
    private int cilindradas;

    public MotorCombustao(int cilindradas) {
        super("Combustão");
        this.cilindradas = cilindradas;
    }

    public int getCilindradas() {
        return cilindradas;
    }
}
