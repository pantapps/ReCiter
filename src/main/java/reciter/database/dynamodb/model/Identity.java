package reciter.database.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "Identity")
@AllArgsConstructor
@NoArgsConstructor
public class Identity {
    private String uid;
    private reciter.model.identity.Identity identity;

    @DynamoDBHashKey(attributeName = "uid")
    public String getUid() {
        return uid;
    }

    @DynamoDBAttribute(attributeName = "identity")
    public reciter.model.identity.Identity getIdentity() {
        return identity;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setIdentity(reciter.model.identity.Identity identity) {
        this.identity = identity;
    }
}
