package awslambda.gateway.aws.dynamodb;

import awslambda.entity.LambdaExecutionTime;

public interface DynamoDbGateway {

    public void saveExecution(LambdaExecutionTime executionTimes);
}
