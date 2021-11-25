package awslambda.gateway.aws.dynamodb;

import awslambda.entity.LambdaExecutionTimes;

public interface DynamoDbGateway {

    public void saveExecution(LambdaExecutionTimes executionTimes);
}
