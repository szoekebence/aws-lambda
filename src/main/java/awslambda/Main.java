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
    private static final AWSLambdaFunctionGateway LAMBDA_FUNCTION_GATEWAY = new AWSLambdaFunctionGatewayImpl();
    private static final DynamoDbGatewayImpl DYNAMO_DB_GATEWAY = new DynamoDbGatewayImpl();

    public static void main(String[] args) {
        for (String functionLanguage : LAMBDA_FUNCTION_LANGUAGES) {

            LambdaExecutionTimes item = new LambdaExecutionTimes();
            item.functionLanguage = functionLanguage;
            item.results = generateResultsByLambdaFunctionLanguage();

            ((DynamoDbGateway) DYNAMO_DB_GATEWAY).saveExecution(item);
        }
    }

    private static Map<String, Map<String, Map<String, Map<String, List<Map<String, Float>>>>>> generateResultsByLambdaFunctionLanguage() {
        Map<String, Map<String, Map<String, Map<String, List<Map<String, Float>>>>>> memorySizesTree = new HashMap<>();
        Map<String, Map<String, Map<String, List<Map<String, Float>>>>> memorySizes = new HashMap<>();
        for (String memorySize : LAMBDA_FUNCTION_MEMORY_SIZE) {
            Map<String, Map<String, List<Map<String, Float>>>> architectureTree = new HashMap<>();
            architectureTree.put("architectures", generateResultsByMemorySize());
            memorySizes.put(memorySize, architectureTree);
            memorySizesTree.put("memorySizes", memorySizes);
        }
        return memorySizesTree;
    }

    private static Map<String, List<Map<String, Float>>> generateResultsByMemorySize() {
        Map<String, List<Map<String, Float>>> architectures = new HashMap<>();
        for (String architecture : LAMBDA_FUNCTION_ARCHITECTURES) {
            architectures.put(architecture, generateResultsByArchitectureType());
        }
        return architectures;
    }

    private static List<Map<String, Float>> generateResultsByArchitectureType() {
        List<Map<String, Float>> measurements = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            float[] lambdaGwResponse = LAMBDA_FUNCTION_GATEWAY.callLambda("szokeb-java-fibonacci", "100");
            Map<String, Float> actualTimes = new HashMap<>();
            actualTimes.put("Lambda execution time (ms)", lambdaGwResponse[0]);
            actualTimes.put("Lambda invoke time (ms)", lambdaGwResponse[1]);
            actualTimes.put("Total time (ms)", lambdaGwResponse[2]);
            measurements.add(actualTimes);
        }
        return measurements;
    }
}
