package supermed.datamanagementsystem.impl.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document(collection="sequence")
public class SequenceNumber {
    private String collectionName;
    private BigInteger nextID;

    public String getCollectionName(){
        return collectionName;
    }

    public String getID(){
        return nextID.toString();
    }
}
