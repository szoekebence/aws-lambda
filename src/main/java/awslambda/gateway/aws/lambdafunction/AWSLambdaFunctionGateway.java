package awslambda.gateway.aws.lambdafunction;

public interface AWSLambdaFunctionGateway {

    float[] callLambda(String functionName, String payload);

}
