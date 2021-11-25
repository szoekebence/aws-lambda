package awslambda.core;

import awslambda.gateway.aws.lambdafunction.AWSLambdaFunctionGateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static awslambda.gateway.aws.AWSCConstants.LAMBDA_FUNCTION_ARCHITECTURES;
import static awslambda.gateway.aws.AWSCConstants.LAMBDA_FUNCTION_MEMORY_SIZE;

public class ResultGenerator {

    private final AWSLambdaFunctionGateway lambdaFunctionGateway;

    public ResultGenerator(AWSLambdaFunctionGateway lambdaFunctionGateway) {
        this.lambdaFunctionGateway = lambdaFunctionGateway;
    }

    public Map<String, Map<String, Map<String, Map<String, List<Map<String, Float>>>>>> generateResultsByLambdaFunctionLanguage() {
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

    private Map<String, List<Map<String, Float>>> generateResultsByMemorySize() {
        Map<String, List<Map<String, Float>>> architectures = new HashMap<>();
        for (String architecture : LAMBDA_FUNCTION_ARCHITECTURES) {
            architectures.put(architecture, generateResultsByArchitectureType());
        }
        return architectures;
    }

    private List<Map<String, Float>> generateResultsByArchitectureType() {
        List<Map<String, Float>> measurements = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            float[] lambdaGwResponse = lambdaFunctionGateway.callLambda("szokeb-java-fibonacci", "100");
            Map<String, Float> actualTimes = new HashMap<>();
            actualTimes.put("Lambda execution time (ms)", lambdaGwResponse[0]);
            actualTimes.put("Lambda invoke time (ms)", lambdaGwResponse[1]);
            actualTimes.put("Total time (ms)", lambdaGwResponse[2]);
            measurements.add(actualTimes);
        }
        return measurements;
    }

}
