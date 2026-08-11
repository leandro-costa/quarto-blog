class TV {
    float tamanhoTela;
    int volume;// volume: de 1 a 10 iniciando em 5 (somente no construtor);
    String marca;
    Voltagem voltagem;
    int canal;
    boolean ligado;

    TV(float tamanhoTela, String marca, Voltagem voltagem) {
        this.tamanhoTela = tamanhoTela;
        this.marca = marca;
        this.voltagem = voltagem;
        this.ligado = false;
        this.volume = 5;
    }

    // ligar: ao ligar a televisão deve imprimir seu consumo. O consumo deve ser
    // definido pela voltagem multiplicada pela quantidades de polegadas;
    float ligar() {
        if (!ligado) {
            ligado = true;
        }
        return voltagem.getValorVoltagem() * tamanhoTela;
    }

    boolean desligar() {
        if (ligado) {
            ligado = false;
        }
        return ligado;
    }

    int aumentarVolume() {
        if ((volume < 10) && (ligado)) {
            volume++;
        }
        return volume;
    }

    int diminuirVolume() {
        if ((volume > 1) && (ligado)) {
            volume--;
        }
        return volume;
    }

    int subirCanal(){
        if(ligado){
            canal++;
        }
        return canal;
    }
    int descerCanal(){
        if((ligado) && (canal > 2)){
            canal--;
        }
        return canal;
    }

}