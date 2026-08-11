public class TestaConta {
    public static void main(String[] args) {
        Banco itau = new Banco();
        String nomeCorrentista1 = "Leandro";
        itau.addCorrentista(nomeCorrentista1);
        System.out.println("Saldo da conta de "+nomeCorrentista1+" :"+itau.getSaldo(nomeCorrentista1));
        itau.creditar(nomeCorrentista1, 500);
        System.out.println("Saldo da conta de "+nomeCorrentista1+" :"+itau.getSaldo(nomeCorrentista1));
        itau.debitar(nomeCorrentista1, 400);
        System.out.println("Saldo da conta de "+nomeCorrentista1+" :"+itau.getSaldo(nomeCorrentista1));
        String nomeCorrentista2 = "Leo";
        itau.addCorrentista(nomeCorrentista2);
        System.out.println("Saldo da conta de "+nomeCorrentista2+" :"+itau.getSaldo(nomeCorrentista2));
        itau.transferir(nomeCorrentista1, nomeCorrentista2, 300);
        System.out.println("Saldo da conta de "+nomeCorrentista1+" :"+itau.getSaldo(nomeCorrentista1));
        System.out.println("Saldo da conta de "+nomeCorrentista2+" :"+itau.getSaldo(nomeCorrentista2));
    }
}
