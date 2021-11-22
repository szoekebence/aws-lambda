package awslambda;

public class JavaFibonacciFunctionHandler {

    public Long handleRequest(String n) {
        long startTime = System.nanoTime();
        int intN = Integer.parseInt(n), i;
        long f1 = 0L, f2 = 1L;
        for (i = 1; i < intN; i++) {
            long next = f1 + f2;
            f1 = f2;
            f2 = next;
        }
        return (System.nanoTime() - startTime) / 1000000;
    }
}