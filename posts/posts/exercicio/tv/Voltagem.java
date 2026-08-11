
public enum Voltagem {
    V110(110), V220(220);

    final int voltagem;
    Voltagem(int voltagem){
        this.voltagem = voltagem;
    }

    public int getValorVoltagem() {
        return voltagem;
    }
}
