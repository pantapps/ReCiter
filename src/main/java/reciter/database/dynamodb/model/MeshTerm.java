package reciter.database.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "MeshTerm")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeshTerm {
    private String mesh;
    private long count;

    @DynamoDBHashKey(attributeName = "mesh")
    public String getMesh() {
        return mesh;
    }

    @DynamoDBAttribute(attributeName = "count")
    public long getCount() {
        return count;
    }
}
