
public class App {

    public static void main(String[] args) throws Exception {

        Agencia ag1 = new Agencia(2);
        int numeroConta1 = ag1.criarConta("Leandro");
        IO.println("Numero da contra de Leandro :" + numeroConta1);
        int numeroConta2 = ag1.criarConta("Isabela");
        IO.println("Numero da contra de Isabela :" + numeroConta2);
        Conta conta1 = ag1.getConta(numeroConta1);
        Conta conta2 = ag1.getConta(numeroConta2);
        // Somente mesmo pacote pode chamar o new
        // Conta conta3 = new Conta("Nome");//erro em classes de outro pacote

        conta1.deposita(100.0);
        conta2.deposita(10.0);

        conta1.transferir(conta2, 50);
        IO.println(conta1.toString());
        IO.println(conta2);
        IO.println(String.format("Total de Contas: %d", ag1.totalContas()));
        IO.println(String.format("Total de Dinheiro: %.2f", ag1.totalDinheiro()));

        ag1.getContas().clear();
        IO.println(String.format("Total de Contas: %d", ag1.totalContas()));
        IO.println(String.format("Total de Dinheiro: %.2f", ag1.totalDinheiro()));
        
    }
}
