public class GenericsTest2 {
    public static void main(String[] args) {
        Par<String, Integer> par = new Par<>("Test String.", 100);
        par.showItemDetails();
    }
}

class Par<T, U> {
    private T itemT;
    private U itemU;

    public Par(T itemT, U itemU) {
        this.itemT = itemT;
        this.itemU = itemU;
    }

    public T getItemT() { return itemT; }
    public U getItemU() { return itemU; }

    public void showItemDetails() {
        System.out.println("Value of itemT: " + itemT + " (Type: " + itemT.getClass().getName() + ")");
        System.out.println("Value of itemU: " + itemU + " (Type: " + itemU.getClass().getName() + ")");
    }
}
