public class JavaFibonacciFunctionHandler {

    public float handleRequest() {
        long startTime = System.nanoTime();
        long f1 = 0L, f2 = 1L;
        for (int i = 0; i < 10000; i++) {
            long next = f1 + f2;
            f1 = f2;
            f2 = next;
        }
        return (System.nanoTime() - startTime) / 1000000F;
    }
}