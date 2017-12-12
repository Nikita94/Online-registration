package supermed.datamanagementsystem.impl.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class MongoSequence {

    @Autowired
    MongoOperations mongoOperations;

    public String getNextID(String collectionName){
        Query query = new Query(Criteria.where("collectionName").is(collectionName));
        Update update = new Update();
        update.inc("nextID", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        SequenceNumber id = mongoOperations.findAndModify(query, update, options, SequenceNumber.class);
        return id.getID();
    }
}
