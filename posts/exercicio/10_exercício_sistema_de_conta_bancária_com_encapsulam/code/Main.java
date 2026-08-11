package contaBancaria;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Criação das Contas ===\n");
        ContaBancaria conta1 = new ContaBancaria("001", "João Silva");
        ContaBancaria conta2 = new ContaBancaria("002", "Maria Santos", 200.0);

        conta1.exibirInformacoes();
        conta2.exibirInformacoes();

        System.out.println("=== Operações na Conta 1 ===\n");
        conta1.depositar(500.0);
        conta1.sacar(100.0);
        conta1.exibirInformacoes();

        System.out.println("=== Operações na Conta 2 ===\n");
        conta2.depositar(300.0);
        conta2.exibirInformacoes();

        System.out.println("=== Transferência da Conta 1 para Conta 2 ===\n");
        conta1.transferir(conta2, 150.0);

        System.out.println("\n=== Estado Final das Contas ===\n");
        conta1.exibirInformacoes();
        conta2.exibirInformacoes();

        System.out.println("=== Testes de Validação ===\n");
        System.out.println("Tentando depósito com valor negativo:");
        conta1.depositar(-50.0);

        System.out.println("\nTentando saque maior que saldo + limite:");
        conta1.sacar(1000.0);

        System.out.println("\nTentando transferência com valor zero:");
        conta1.transferir(conta2, 0);

        System.out.println("\n=== Estado após validações ===\n");
        conta1.exibirInformacoes();
    }
}