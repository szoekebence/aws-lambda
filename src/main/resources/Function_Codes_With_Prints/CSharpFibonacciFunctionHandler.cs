public class JavaFibonacciFunctionHandler {

    public long handleRequest() {
        Stopwatch stopwatch = Stopwatch.StartNew();
        long f1 = 0L, f2 = 1L;
        for (int i = 1; i < 100; i++) {
            Console.WriteLine(f1);
            long next = f1 + f2;
            f1 = f2;
            f2 = next;
        }
        stopwatch.Stop();
        return (System.nanoTime() - startTime);
    }
}//todo timert megcsinálni nanosecre vagy secre