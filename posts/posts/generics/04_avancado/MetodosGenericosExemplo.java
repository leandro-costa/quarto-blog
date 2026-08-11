public class MetodosGenericosExemplo {

    // Método genérico para imprimir elementos de qualquer tipo de array
    public static <E> void imprimirArray(E[] array) {
        for (E elemento : array) {
            System.out.print(elemento + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] numeros = { 1, 2, 3, 4, 5 };
        String[] nomes = { "Ana", "Bia", "Carlos" };
        Double[] decimais = { 1.5, 2.5, 3.5 };

        System.out.print("Array de Inteiros: ");
        imprimirArray(numeros);

        System.out.print("Array de Strings: ");
        imprimirArray(nomes);

        System.out.print("Array de Decimais: ");
        imprimirArray(decimais);
    }
}
