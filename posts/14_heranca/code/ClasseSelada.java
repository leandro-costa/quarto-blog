
sealed public class ClasseSelada permits SubClasse1, SubClasse2 {
    public void metodo() {
        IO.println("Método da classe selada");
    }
}   