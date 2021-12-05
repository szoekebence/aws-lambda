package awslambda.gateway.aws.lambdafunction;

import awslambda.entity.LambdaExecutionTime;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.awaitility.Awaitility;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Stream;

import static awslambda.gateway.aws.AWSConstants.*;
import static java.util.concurrent.TimeUnit.SECONDS;

public class AWSLambdaFunctionGatewayImpl implements AWSLambdaFunctionGateway {

    private static final TypeReference<Map<String, Float>> TYPE_REF = new TypeReference<>() {
    };
    private final AWSLambda AWSLambda;
    private final ObjectMapper objectMapper;
    private String actualFunctionName;
    private String actualMemorySize;
    private String actualArchitecture;

    public AWSLambdaFunctionGatewayImpl() {
        AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        AWSLambda = AWSLambdaClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        objectMapper = Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build();
    }

    @Override
    public void configurateLambdaFunction(String functionLanguage, String memorySize, String architecture) {
        updateFunctionConfig(functionLanguage, memorySize);
        waitForLambdaFunctionReadiness();
        updateFunctionCode(architecture);
        waitForLambdaFunctionReadiness();
    }

    @Override
    public LambdaExecutionTime callLambda() {
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
        actualMemorySize = String.valueOf(response.getMemorySize());
    }

    private void updateFunctionCode(String architecture) {
        UpdateFunctionCodeRequest updateFunctionCodeRequest = new UpdateFunctionCodeRequest()
                .withFunctionName(actualFunctionName)
                .withArchitectures(architecture)
                .withZipFile(generateZipAsByteBuffer());
        UpdateFunctionCodeResult response = AWSLambda.updateFunctionCode(updateFunctionCodeRequest);
        actualArchitecture = response.getArchitectures().get(0);
    }

    private ByteBuffer generateZipAsByteBuffer() {
        try (RandomAccessFile raf = new RandomAccessFile(
                generateArchivedFunctionCode(), "r"); FileChannel channel = raf.getChannel()) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            buffer.load();
            return buffer;
        } catch (IOException e) {
            throw new RuntimeException("Zip file reading failed!");
        }
    }

    private File generateArchivedFunctionCode() {
        if (actualFunctionName.contains("java")) {
            return new File("src/main/resources/Function_Code_Archives_10000/" + actualFunctionName + ".jar");
        }
        return new File("src/main/resources/Function_Code_Archives_10000/" + actualFunctionName + ".zip");
    }

    private InvokeRequest generateInvokeRequest() {
        return new InvokeRequest()
                .withFunctionName(actualFunctionName)
                .withInvocationType(InvocationType.RequestResponse);
    }

    private LambdaExecutionTime doCallLambda(InvokeRequest lmbRequest) {
        Instant startTime = Instant.now();
        InvokeResult lmbResult = AWSLambda.invoke(lmbRequest);
        Instant endTime = Instant.now();
        float lambdaExecTime;
        if (Stream.of("szokeb-java-fibonacci", "szokeb-python-fibonacci", "szokeb-nodejs-fibonacci")
                .anyMatch(x -> x.equals(actualFunctionName))) {
            lambdaExecTime = Float.parseFloat(new String(lmbResult.getPayload().array(), StandardCharsets.UTF_8));
        } else if ("szokeb-ruby-fibonacci".equals(actualFunctionName)) {
            lambdaExecTime = collectRubyFunctionResponse(lmbResult);
        } else {
            throw new RuntimeException("Unexpected Lambda Function name!");
        }

        return new LambdaExecutionTime()
                .setFunctionLanguage(generateActualFunctionLanguage())
                .setMemorySize(actualMemorySize)
                .setArchitecture(actualArchitecture)
                .setExecutionTime(lambdaExecTime)                                                               //Lambda execution time in ms
                .setInvokeTime((Duration.between(startTime, endTime).toNanos() / 1000000.0F) - lambdaExecTime)  //Invoke time in ms
                .setTotalTime(Duration.between(startTime, endTime).toNanos() / 1000000.0F);                     //Total time in ms
    }

    private String generateActualFunctionLanguage() {
        return LAMBDA_FUNCTION_LANGUAGES.stream().filter(x -> actualFunctionName.contains(x)).findFirst().get();
    }

    private float collectRubyFunctionResponse(InvokeResult lmbResult) {
        String response = new String(lmbResult.getPayload().array(), StandardCharsets.UTF_8);
        try {
            return objectMapper.readValue(response, TYPE_REF).get("body");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Exception occurred while converting json to map.");
        }
    }
}
