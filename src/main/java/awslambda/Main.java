package awslambda;

import awslambda.entity.LambdaExecutionTimes;
import awslambda.gateway.aws.dynamodb.DynamoDbGateway;
import awslambda.gateway.aws.dynamodb.DynamoDbGatewayImpl;
import awslambda.gateway.aws.lambdafunction.AWSLambdaFunctionGateway;
import awslambda.gateway.aws.lambdafunction.AWSLambdaFunctionGatewayImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final List<String> LAMBDA_FUNCTION_LANGUAGES = List.of("java");
    private static final List<String> LAMBDA_FUNCTION_ARCHITECTURES = List.of("X86");
    private static final List<String> LAMBDA_FUNCTION_MEMORY_SIZE = List.of("1769MB");

    public static void main(String[] args) {
        DynamoDbGateway dynamoDbGateway = new DynamoDbGatewayImpl();
        AWSLambdaFunctionGateway lambdaFunctionGateway = new AWSLambdaFunctionGatewayImpl();

        for (String functionLanguage : LAMBDA_FUNCTION_LANGUAGES) {
            Map<String, Map<String, Map<String, Map<String, List<Map<String, Float>>>>>> memorySizesTree = new HashMap<>();
            Map<String, Map<String, Map<String, List<Map<String, Float>>>>> memorySizes = new HashMap<>();
            for (String memorySize : LAMBDA_FUNCTION_MEMORY_SIZE) {
                Map<String, Map<String, List<Map<String, Float>>>> architectureTree = new HashMap<>();
                Map<String, List<Map<String, Float>>> architectures = new HashMap<>();
                for (String architecture : LAMBDA_FUNCTION_ARCHITECTURES) {
                    List<Map<String, Float>> measurements = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        float[] lambdaGwResponse = lambdaFunctionGateway.callLambda("szokeb-java-fibonacci", "100");
                        Map<String, Float> actualTimes = new HashMap<>();
                        actualTimes.put("Lambda execution time (ms)", lambdaGwResponse[0]);
                        actualTimes.put("Lambda invoke time (ms)", lambdaGwResponse[1]);
                        actualTimes.put("Total time (ms)", lambdaGwResponse[2]);
                        measurements.add(actualTimes);
                    }
                    architectures.put(architecture, measurements);
                }
                architectureTree.put("architectures", architectures);
                memorySizes.put(memorySize, architectureTree);
                memorySizesTree.put("memorySizes", memorySizes);
            }
            LambdaExecutionTimes item = new LambdaExecutionTimes();
            item.functionLanguage = functionLanguage;
            item.results = memorySizesTree;
            dynamoDbGateway.saveExecution(item);
        }
    }

//    private static LambdaExecutionTimes createItem() {
////        Map<String, Map<String, Map<Integer, Map<String, Float>>>>
//        LambdaExecutionTimes result = new LambdaExecutionTimes();
//        result.functionLanguage = "sql";
//        result.results = Map.of(
//                "1769MB", Map.of("architectures",
//                        Map.of(
//                                "x86", List.of(
//                                        Map.of("execTime (ms)", 0.1F, "invokeTime (ms)", 0.2f, "totalTime (ms)", 0.3f),
//                                        Map.of("execTime (ms)", 0.4F, "invokeTime (ms)", 0.5f, "totalTime (ms)", 0.6f)),
//                                "arm", List.of(
//                                        Map.of("execTime (ms)", 0.7F, "invokeTime (ms)", 0.8f, "totalTime (ms)", 0.9f),
//                                        Map.of("execTime (ms)", 1.0F, "invokeTime (ms)", 1.1f, "totalTime (ms)", 1.2f)))));
//        return result;
//    }
}
