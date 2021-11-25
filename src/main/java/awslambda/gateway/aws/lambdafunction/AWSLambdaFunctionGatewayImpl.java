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

import static awslambda.gateway.aws.AWSConstants.*;

public class AWSLambdaFunctionGatewayImpl implements AWSLambdaFunctionGateway {

    private final AWSLambda lambda;
    private String actualFunctionLanguage;
    private String actualMemorySize;
    private String actualArchitecture;

    public AWSLambdaFunctionGatewayImpl() {
        AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        lambda = AWSLambdaClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    @Override
    public float[] callLambda(String functionLanguage, String memorySize, String architecture) {
        refreshFunctionConfigurationsIfNeeded(functionLanguage, memorySize, architecture);
        InvokeRequest lmbRequest = generateInvokeRequest();
        return doCallLambda(lmbRequest);
    }

    private void refreshFunctionConfigurationsIfNeeded(String functionLanguage, String memorySize,
                                                       String architecture) {
        if (isFunctionConfigurationChanged(functionLanguage, memorySize, architecture)) {
            actualFunctionLanguage = functionLanguage;
            actualMemorySize = memorySize;
            actualArchitecture = architecture;
            //todo: config change
        }
    }

    private boolean isFunctionConfigurationChanged(String functionLanguage, String memorySize, String architecture) {
        return !functionLanguage.equals(actualFunctionLanguage) ||
                !memorySize.equals(actualMemorySize) ||
                !architecture.equals(actualArchitecture);
    }

    private InvokeRequest generateInvokeRequest() {
        return new InvokeRequest()
                .withFunctionName("szokeb-" + actualFunctionLanguage + "-fibonacci")
                .withPayload(NUMBERS_OF_FIBONACCIS_TO_CALCULATE)
                .withInvocationType(InvocationType.RequestResponse);
    }

    private float[] doCallLambda(InvokeRequest lmbRequest) {
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
