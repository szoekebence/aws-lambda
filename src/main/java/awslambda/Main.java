package awslambda;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

import java.nio.charset.StandardCharsets;

public class Main {

    private static final String AWS_ACCESS_KEY_ID = "AKIA46D2LTA52FLZ5NGR";
    private static final String AWS_SECRET_ACCESS_KEY = "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";

    public static void main(String[] args) {
        AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        InvokeRequest lmbRequest = new InvokeRequest()
                .withFunctionName("szokeb-java-fibonacci")
                .withPayload("50");
        lmbRequest.setInvocationType(InvocationType.RequestResponse);
        AWSLambda lambda = AWSLambdaClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        InvokeResult lmbResult = lambda.invoke(lmbRequest);

        String resultJSON = new String(lmbResult.getPayload().array(), StandardCharsets.UTF_8);

        System.out.println(resultJSON);
    }

}
