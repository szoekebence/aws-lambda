package awslambda.gateway.aws.lambdafunction;

public interface AWSLambdaFunctionGateway {

    void configurateLambdaFunction(String functionLanguage, String memorySize, String architecture);

    float[] callLambda();
}
