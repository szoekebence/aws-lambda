public class JavaFibonacciFunctionHandler {

    public float handleRequest() {
        long startTime = System.nanoTime();
        int x = fibonacci(28);
        long duration = System.nanoTime() - startTime;
        return duration * 0.000001F;
    }

    private int fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return (fibonacci(n - 1) + fibonacci(n - 2));
    }
}