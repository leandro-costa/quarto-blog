import java.time.LocalDate;

class Emprestimo extends Servico {
    // ATRIBUTOS
    public Emprestimo(LocalDate date){
        super(date);
        IO.println("Emprestimo");
    }

    
    public static void main(String[] args) {
        Emprestimo e  = new Emprestimo(LocalDate.now().plusDays(1));
        e.getDate();
    }
}