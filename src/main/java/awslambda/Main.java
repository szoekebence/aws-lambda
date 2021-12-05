package awslambda;

import awslambda.gateway.aws.dynamodb.DynamoDbGatewayImpl;
import awslambda.gateway.aws.lambdafunction.AWSLambdaFunctionGatewayImpl;

import static awslambda.gateway.aws.AWSConstants.*;

public class Main {

    public static void main(String[] args) {
        AWSLambdaFunctionGatewayImpl lambdaFunctionGateway = new AWSLambdaFunctionGatewayImpl();
        DynamoDbGatewayImpl dynamoDbGateway = new DynamoDbGatewayImpl();

        for (String functionLanguage : LAMBDA_FUNCTION_LANGUAGES) {
            for (String memorySize : LAMBDA_FUNCTION_MEMORY_SIZE) {
                for (String architecture : LAMBDA_FUNCTION_ARCHITECTURES) {
                    lambdaFunctionGateway.configurateLambdaFunction(functionLanguage, memorySize, architecture);
                    for (int i = 0; i < NUMBER_OF_FUNCTION_CALLS_PER_CONFIGURATION; i++) {
                        dynamoDbGateway.saveExecution(lambdaFunctionGateway.callLambda());
                    }
                }
            }
        }
    }
}
