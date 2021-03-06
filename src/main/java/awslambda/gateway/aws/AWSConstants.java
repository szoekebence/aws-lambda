package awslambda.gateway.aws;

import java.util.List;

public final class AWSConstants {

    public static final String DB_HOST = "https://dynamodb.eu-central-1.amazonaws.com";
    public static final String REGION = "eu-central-1";
    public static final String AWS_ACCESS_KEY_ID = null;
    public static final String AWS_SECRET_ACCESS_KEY = null;
    public static final int NUMBER_OF_FUNCTION_CALLS_PER_CONFIGURATION = 100;
    public static final List<String> LAMBDA_FUNCTION_LANGUAGES = List.of("java", "python", "nodejs", "ruby");
    public static final List<String> LAMBDA_FUNCTION_ARCHITECTURES = List.of("x86_64", "arm64");
    public static final List<String> LAMBDA_FUNCTION_MEMORY_SIZE = List.of("128", "256", "512", "1024", "1769");

    private AWSConstants() {
    }
}