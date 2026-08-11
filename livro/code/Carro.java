class Carro {
    String modelo;
    int velocidade;
    int aceleracao;
    int marcha;
    boolean ligado;

    Carro(String modelo) {
        this.modelo = modelo;
        ligado = false;
        velocidade = 0;
        marcha = 0;
    }

    boolean ligar() {
        if (!ligado) {
            ligado = true;
        }
        return ligado;
    }

    boolean desligar() {
        if (ligado) {
            ligado = false;
        }
        return ligado;
    }

    int acelerar() {
        if (ligado) {
            velocidade+=marcha;
        }
        return velocidade;
    }

    int desacelerar() {
        if (velocidade > 0) {
            velocidade--;
        }
        return velocidade;
    }

    String virarDireita(){
        return "Direita";
    }

    String virarEsquerda(){
        return "Esquerda";
    }
    
    int marchaCima(){
        if(marcha < 6){
            marcha++;
        }
        return marcha;
    }

    int marchaAbaixo(){
        if(marcha > -1){
            marcha--;
        }
        return marcha;
    }

}