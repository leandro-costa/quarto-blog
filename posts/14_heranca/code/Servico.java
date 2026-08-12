import java.time.LocalDate;

class Servico extends Object {
    private LocalDate date;

    // ATRIBUTOS
    public Servico(LocalDate data){
        if(data != null && LocalDate.now().isAfter(data)){
            this.date = date;
        }
    }

    public LocalDate getDate() {
        return date;
    }
}