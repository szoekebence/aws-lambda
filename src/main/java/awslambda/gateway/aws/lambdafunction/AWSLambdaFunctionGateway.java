package awslambda.gateway.aws.lambdafunction;

public interface AWSLambdaFunctionGateway {

    void configurateLambdaFunction(String functionLanguage, String memorySize, String architecture);

    boolean checkAvailability();

    float[] callLambda();
}
