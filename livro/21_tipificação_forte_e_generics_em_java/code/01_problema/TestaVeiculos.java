public class TestaVeiculos {
    public static void main(String[] args) {
        Carro carro = new Carro("Sedan", new MotorCombustao(2000));

        // getMotor() retorna Motor. Para acessar métodos específicos, EXIGE CAST!
        Motor motorGenerico = carro.getMotor();
        MotorCombustao motorCombustao = (MotorCombustao) motorGenerico; // Cast explícito!

        System.out.println("Modelo: " + carro.getModelo());
        System.out.println("Cilindradas: " + motorCombustao.getCilindradas());
    }
}
