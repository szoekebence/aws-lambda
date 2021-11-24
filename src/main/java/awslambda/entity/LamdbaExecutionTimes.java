package awslambda.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Map;

@DynamoDBTable(tableName = "LamdbaExecutionTimes")
public class LamdbaExecutionTimes {

    public String functionLanguage;
    public Map<String, List<Map<String, Float>>> architecture;

    @DynamoDBHashKey(attributeName = "functionLanguage")
    public String getFunctionLanguage() {
        return functionLanguage;
    }

    public void setFunctionLanguage(String functionLanguage) {
        this.functionLanguage = functionLanguage;
    }

    @DynamoDBAttribute(attributeName = "architecture")
    public Map<String, List<Map<String, Float>>> getArchitecture() {
        return architecture;
    }

    public void setArchitecture(Map<String, List<Map<String, Float>>> architecture) {
        this.architecture = architecture;
    }

    public LamdbaExecutionTimes() {
    }
}
