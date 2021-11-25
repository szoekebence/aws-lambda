package awslambda.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Map;

@DynamoDBTable(tableName = "LamdbaExecutionTimes")
public class LambdaExecutionTimes {

    public String functionLanguage;
    public Map<String, Map<String, List<Map<String, Float>>>> memorySize;

    @DynamoDBHashKey(attributeName = "functionLanguage")
    public String getFunctionLanguage() {
        return functionLanguage;
    }

    public void setFunctionLanguage(String functionLanguage) {
        this.functionLanguage = functionLanguage;
    }

    @DynamoDBAttribute(attributeName = "memorySize")
    public Map<String, Map<String, List<Map<String, Float>>>> getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(Map<String, Map<String, List<Map<String, Float>>>> memorySize) {
        this.memorySize = memorySize;
    }

    public LambdaExecutionTimes() {
    }
}
