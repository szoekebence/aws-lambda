package awslambda.gateway.aws.lambdafunction;

public interface AWSLambdaFunctionGateway {

    float[] callLambda(String functionLanguage, String memorySize, String architecture);

}
