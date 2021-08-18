package util;

import constants.ConstantsVertX;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;


public class ConnectMongoDB {
    private static final String URI_MONGODB = ConstantsVertX.URI_MONGODB;
    private static final String DB_NAME = ConstantsVertX.DB_NAME;

    public static MongoClient connectMongoClient() {
        Vertx vertx = Vertx.vertx();
        JsonObject jsonObject = new JsonObject()
                .put("connection_string", URI_MONGODB)
                .put("db_name", DB_NAME);
        MongoClient mongoClient = MongoClient.createShared(vertx, jsonObject);
        mongoClient.getCollections(asyncRs -> {
            if (asyncRs.succeeded()){
                System.out.println("connect Database successfully");
            }else {
                System.err.println(asyncRs.cause().getMessage());
            }
        });
        return mongoClient;
    }
}
