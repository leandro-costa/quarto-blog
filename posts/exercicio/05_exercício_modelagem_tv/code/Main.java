void main(){
    TV tv = new TV(55, "LG", Voltagem.V220);
    IO.println("Ligando a TV");
    IO.println("Cosumo:"+tv.ligar());
}