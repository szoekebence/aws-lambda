package awslambda.gateway.aws.lambdafunction;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.*;
import org.awaitility.Awaitility;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import static awslambda.gateway.aws.AWSConstants.AWS_ACCESS_KEY_ID;
import static awslambda.gateway.aws.AWSConstants.AWS_SECRET_ACCESS_KEY;
import static java.util.concurrent.TimeUnit.SECONDS;

public class AWSLambdaFunctionGatewayImpl implements AWSLambdaFunctionGateway {

    private final AWSLambda AWSLambda;
    private String actualFunctionName;

    public AWSLambdaFunctionGatewayImpl() {
        AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        AWSLambda = AWSLambdaClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    @Override
    public void configurateLambdaFunction(String functionLanguage, String memorySize, String architecture) {
        updateFunctionConfig(functionLanguage, memorySize);
        waitForLambdaFunctionReadiness();
        updateFunctionCode(architecture);
        waitForLambdaFunctionReadiness();
    }

    @Override
    public float[] callLambda() {
        InvokeRequest lmbRequest = generateInvokeRequest();
        return doCallLambda(lmbRequest);
    }

    private void waitForLambdaFunctionReadiness() {
        Awaitility.with()
                .pollInterval(1, SECONDS)
                .atMost(10, SECONDS)
                .await()
                .until(this::checkAvailability);
    }

    private boolean checkAvailability() {
        GetFunctionConfigurationRequest request = new GetFunctionConfigurationRequest()
                .withFunctionName(actualFunctionName);
        GetFunctionConfigurationResult response = AWSLambda.getFunctionConfiguration(request);
        return "Active".equals(response.getState());
    }

    private void updateFunctionConfig(String functionLanguage, String memorySize) {
        UpdateFunctionConfigurationRequest request = new UpdateFunctionConfigurationRequest()
                .withFunctionName("szokeb-" + functionLanguage + "-fibonacci")
                .withMemorySize(Integer.valueOf(memorySize));
        UpdateFunctionConfigurationResult response = AWSLambda.updateFunctionConfiguration(request);
        actualFunctionName = response.getFunctionName();
    }

    private void updateFunctionCode(String architecture) {
        UpdateFunctionCodeRequest updateFunctionCodeRequest = new UpdateFunctionCodeRequest()
                .withFunctionName(actualFunctionName)
                .withArchitectures(architecture)
                .withZipFile(generateZipAsByteBuffer());
        AWSLambda.updateFunctionCode(updateFunctionCodeRequest);
    }

    private ByteBuffer generateZipAsByteBuffer() {
        try (RandomAccessFile raf = new RandomAccessFile(
                generateCompressedFunctionCode(), "r"); FileChannel channel = raf.getChannel()) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            buffer.load();
            return buffer;
        } catch (IOException e) {
            throw new RuntimeException("Zip file reading failed!");
        }
    }

    private File generateCompressedFunctionCode() {
        if (actualFunctionName.contains("java")) {
            return new File("src/main/resources/Function_Code_Archives_1000/" + actualFunctionName + ".jar");
        }
        return new File("src/main/resources/Function_Code_Archives_1000/" + actualFunctionName + ".zip");
    }

    private InvokeRequest generateInvokeRequest() {
        return new InvokeRequest()
                .withFunctionName(actualFunctionName)
                .withInvocationType(InvocationType.RequestResponse);
    }

    private float[] doCallLambda(InvokeRequest lmbRequest) {
        float startTime = System.nanoTime() / 1000000F;
        InvokeResult lmbResult = AWSLambda.invoke(lmbRequest);
        float endTime = System.nanoTime() / 1000000F;
        float lambdaExecTime = Float.parseFloat(new String(lmbResult.getPayload().array(), StandardCharsets.UTF_8));

        return new float[]{//todo: megvizsgálni időszámítást
                lambdaExecTime,                                                             //Lambda execution time in ms
                (endTime - startTime) > lambdaExecTime
                        ? ((endTime - startTime) - lambdaExecTime) : (endTime - startTime), //Invoke time in ms
                (endTime + startTime)};                                                     //Total time in ms
    }
}
