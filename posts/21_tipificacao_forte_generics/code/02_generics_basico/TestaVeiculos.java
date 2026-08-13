public class TestaVeiculos {
    public static void main(String[] args) {
        Carro carro = new Carro("Sedan", new MotorCombustao(2000));

        // getMotor() retorna MotorCombustao DIRETO! Sem necessidade de Cast!
        MotorCombustao motor = carro.getMotor();
        System.out.println("Modelo: " + carro.getModelo());
        System.out.println("Cilindradas: " + motor.getCilindradas());
    }
}
