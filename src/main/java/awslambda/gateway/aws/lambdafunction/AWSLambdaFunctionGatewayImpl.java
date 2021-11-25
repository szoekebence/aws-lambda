package awslambda.gateway.aws.lambdafunction;

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

import static awslambda.gateway.aws.AWSCConstants.AWS_ACCESS_KEY_ID;
import static awslambda.gateway.aws.AWSCConstants.AWS_SECRET_ACCESS_KEY;

public class AWSLambdaFunctionGatewayImpl implements AWSLambdaFunctionGateway {

    private final AWSLambda lambda;

    public AWSLambdaFunctionGatewayImpl() {
        AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        lambda = AWSLambdaClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    @Override
    public float[] callLambda(String functionName, String payload) {
        InvokeRequest lmbRequest = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload)
                .withInvocationType(InvocationType.RequestResponse);

        long startTime = System.nanoTime();
        InvokeResult lmbResult = lambda.invoke(lmbRequest);
        long endTime = System.nanoTime();
        float lambdaExecTime = Float.parseFloat(new String(lmbResult.getPayload().array(), StandardCharsets.UTF_8));

        return new float[]{
                (lambdaExecTime / 1000000F),                                //Lambda execution time in ms
                (((endTime - startTime) - lambdaExecTime) / 1000000F),      //Invoke time in ms
                ((endTime - startTime) / 1000000F)};                        //Total time in ms
    }
}
