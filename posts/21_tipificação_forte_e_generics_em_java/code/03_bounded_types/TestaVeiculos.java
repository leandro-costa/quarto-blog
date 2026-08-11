public class TestaVeiculos {
    public static void main(String[] args) {
        // Criando motores
        MotorCombustao motorCarro = new MotorCombustao(2000);
        MotorCombustao motorMoto = new MotorCombustao(600);
        MotorEletrico motorCaminhao = new MotorEletrico(300);

        // Criando veículos
        Carro carro = new Carro("Sedan", motorCarro);
        Moto moto = new Moto("Esportiva", motorMoto);
        Caminhao caminhao = new Caminhao("Carga Pesada", motorCaminhao);

        // Acesso direto ao tipo específico sem necessidade de cast!
        IO.println("Cilindradas do Carro: " + carro.getMotor().getCilindradas());
        IO.println("Cilindradas da Moto: " + moto.getMotor().getCilindradas());
        IO.println("Potência do Caminhão: " + caminhao.getMotor().getPotenciaKW() + " kW");

        carro.ligar();
        moto.ligar();
        caminhao.ligar();
    }
}
