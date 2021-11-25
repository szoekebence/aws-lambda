package awslambda.gateway.aws.dynamodb;

import awslambda.entity.LambdaExecutionTimes;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import static awslambda.gateway.aws.AWSCredentials.AWS_ACCESS_KEY_ID;
import static awslambda.gateway.aws.AWSCredentials.AWS_SECRET_ACCESS_KEY;

public class DynamoDbGatewayImpl implements DynamoDbGateway {

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
    public void saveExecution(LambdaExecutionTimes executionTimes) {
        dynamoDBMapper.save(executionTimes);
    }
}
