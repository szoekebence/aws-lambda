package awslambda;

import awslambda.entity.LamdbaExecutionTimes;
import awslambda.gateway.dynamodb.DynamoDbGateway;
import awslambda.gateway.dynamodb.DynamoDbGatewayImpl;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
//        AWSLambdaFunctionGateway lambdaFunctionGateway = new AWSLambdaFunctionGatewayImpl();
        DynamoDbGateway dynamoDbGateway = new DynamoDbGatewayImpl();
        dynamoDbGateway.saveExecution(createItem());
    }

    private static LamdbaExecutionTimes createItem() {
        LamdbaExecutionTimes result = new LamdbaExecutionTimes();
        result.functionLanguage = "java";
        result.architecture = Map.of(
                "x86", List.of(
                        Map.of("execTime (ms)", 0.1F, "invokeTime (ms)", 0.2f, "totalTime (ms)", 0.3f),
                        Map.of("execTime (ms)", 0.4F, "invokeTime (ms)", 0.5f, "totalTime (ms)", 0.6f)),
                "arm", List.of(
                        Map.of("execTime (ms)", 0.7F, "invokeTime (ms)", 0.8f, "totalTime (ms)", 0.9f),
                        Map.of("execTime (ms)", 1.0F, "invokeTime (ms)", 1.1f, "totalTime (ms)", 1.2f)));
        return result;
    }
}
