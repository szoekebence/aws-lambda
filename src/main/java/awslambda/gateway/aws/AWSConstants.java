package awslambda.gateway.aws;

import java.util.List;

public final class AWSConstants {

    public static final String AWS_ACCESS_KEY_ID = "AKIA46D2LTA52FLZ5NGR";
    public static final String AWS_SECRET_ACCESS_KEY = "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";
    public static final int NUMBER_OF_CALLS = 100;
    public static final List<String> LAMBDA_FUNCTION_LANGUAGES = List.of("java"/*, "python", "nodejs"*/);
    public static final List<String> LAMBDA_FUNCTION_ARCHITECTURES = List.of("x86_64"/*, "arm64"*/);
    //    public static final List<String> LAMBDA_FUNCTION_MEMORY_SIZE = List.of("128", "1769");
    public static final List<String> LAMBDA_FUNCTION_MEMORY_SIZE = List.of("1769");

    private AWSConstants() {
    }
}