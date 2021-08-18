package util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import constants.ConstantsVertX;
import org.bson.Document;


public class ConnectMongoDB {
    private static final String URI_MONGODB = ConstantsVertX.URI_MONGODB;
    private static final String DB_NAME = ConstantsVertX.DB_NAME;
    private static final String COLLECTION_NAME = ConstantsVertX.COLLECTION_NAME;

    public static MongoCollection<Document> getCollection() {
        MongoClient mongoClient = MongoClients.create(URI_MONGODB);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        return database.getCollection(COLLECTION_NAME);
    }
}
