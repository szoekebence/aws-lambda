package awslambda.gateway.lambdafunction;

public interface AWSLambdaFunctionGateway {

    float[] callLambda(String functionName, String payload);

}
