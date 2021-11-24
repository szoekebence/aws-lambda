package awslambda.gateway.dynamodb;

import awslambda.entity.LamdbaExecutionTimes;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class DynamoDbGatewayImpl implements DynamoDbGateway {

    private static final String AWS_ACCESS_KEY_ID = "AKIA46D2LTA52FLZ5NGR";
    private static final String AWS_SECRET_ACCESS_KEY = "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";
    private static final String DB_HOST = "https://dynamodb.eu-central-1.amazonaws.com";
    private static final String REGION = "eu-central-1";

    private final DynamoDBMapper dynamoDBMapper;

    public DynamoDbGatewayImpl() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(DB_HOST, REGION))
                .build();
        dynamoDBMapper = new DynamoDBMapper(client);
    }

    @Override
    public void saveExecution(LamdbaExecutionTimes executionTimes) {
        dynamoDBMapper.save(executionTimes);
    }
}
