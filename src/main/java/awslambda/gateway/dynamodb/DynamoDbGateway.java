package awslambda.gateway.dynamodb;

import awslambda.entity.LamdbaExecutionTimes;

public interface DynamoDbGateway {

    public void saveExecution(LamdbaExecutionTimes executionTimes);
}
