package awslambda.gateway.aws.lambdafunction;

import awslambda.entity.LambdaExecutionTime;

public interface AWSLambdaFunctionGateway {

    void configurateLambdaFunction(String functionLanguage, String memorySize, String architecture);

    LambdaExecutionTime callLambda();
}
