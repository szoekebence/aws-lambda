public class JavaFibonacciFunctionHandler {

    public long handleRequest(int n) {
        long startTime = System.nanoTime();
        long f1 = 0L, f2 = 1L;
        for (int i = 1; i < n; i++) {
            long next = f1 + f2;
            f1 = f2;
            f2 = next;
            System.out.println(f1 + "\n");
        }
        return (System.nanoTime() - startTime);
    }
}