package model;

import com.mongodb.client.MongoCollection;
import entity.SmartPhone;
import org.bson.Document;
import util.ConnectMongoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class SmartPhoneModel {

    public void save(Document document) {
        MongoCollection<Document> collection = ConnectMongoDB.getCollection();
        collection.insertOne(document);
    }

    public List<SmartPhone> getAll() {
        MongoCollection<Document> collection = ConnectMongoDB.getCollection();
        List<SmartPhone> listSmartphone = new ArrayList<>();
        collection.find().forEach((Consumer<? super Document>) document -> {
            String id = document.get("_id").toString();
            String name = document.getString("name");
            String description = document.getString("description");
            String price = document.getString("price");
            SmartPhone smartPhone = new SmartPhone(id, name, description, price);
            listSmartphone.add(smartPhone);
        });
        return listSmartphone;
    }

    public void updateById(Document queryById, Document document) {
        MongoCollection<Document> collection = ConnectMongoDB.getCollection();
        Document update = new Document();
        update.append("$set",document);
        collection.updateOne(queryById,update);
    }
//
    public void delete(Document queryId) {
        MongoCollection<Document> collection = ConnectMongoDB.getCollection();
        collection.deleteOne(queryId);
    }
}
