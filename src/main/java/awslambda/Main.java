package awslambda;

import awslambda.core.ResultGenerator;
import awslambda.entity.LambdaExecutionTimes;
import awslambda.gateway.aws.dynamodb.DynamoDbGatewayImpl;
import awslambda.gateway.aws.lambdafunction.AWSLambdaFunctionGatewayImpl;

import static awslambda.gateway.aws.AWSCConstants.LAMBDA_FUNCTION_LANGUAGES;

public class Main {

    public static void main(String[] args) {
        AWSLambdaFunctionGatewayImpl lambdaFunctionGateway = new AWSLambdaFunctionGatewayImpl();
        ResultGenerator resultGenerator = new ResultGenerator(lambdaFunctionGateway);
        DynamoDbGatewayImpl dynamoDbGateway = new DynamoDbGatewayImpl();

        for (String functionLanguage : LAMBDA_FUNCTION_LANGUAGES) {

            LambdaExecutionTimes item = new LambdaExecutionTimes();
            item.functionLanguage = functionLanguage;
            item.results = resultGenerator.generateResultsByLambdaFunctionLanguage();

            dynamoDbGateway.saveExecution(item);
        }
    }
}
