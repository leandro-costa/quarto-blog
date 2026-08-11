public class Credito extends Operacao {

    public Credito(double valor) {
        super(valor);
    }
    @Override
    public double operar() {
        return getValor();
    }    
}
