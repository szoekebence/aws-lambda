package awslambda;

import com.amazonaws.services.lambda.runtime.Context;

public class JavaFibonacciFunctionHandler {

    public String handleRequest(String n, Context context) {
        int intN = Integer.parseInt(n), i;
        long f1 = 0L, f2 = 1L;
        for (i = 1; i < intN; i++) {
            long next = f1 + f2;
            f1 = f2;
            f2 = next;
        }
        return String.valueOf(context.getRemainingTimeInMillis());
    }
}