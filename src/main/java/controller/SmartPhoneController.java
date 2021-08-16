package controller;

import constants.ConstantsVertX;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import model.SmartPhone;
import util.APIRespone;

import java.util.ArrayList;
import java.util.List;

public class SmartPhoneController {
    private static final String COLLECTION_NAME = ConstantsVertX.COLLECTION_NAME;
    private static final String MESSAGE_FAIL = ConstantsVertX.MESSAGE_FAIL;
    private static final String MESSAGE_SUCCESS = ConstantsVertX.MESSAGE_SUCCESS;
    private static final int STATUS_OK = ConstantsVertX.STATUS_OK;
    private static final int STATUS_FAIL = ConstantsVertX.STATUS_FAIL;

    public void save(MongoClient mongo, RoutingContext ctx) {
        APIRespone apiRespone = new APIRespone();
        JsonObject newProduct = ctx.getBodyAsJson();
        mongo.insert(COLLECTION_NAME, newProduct, res -> {
            if (res.succeeded()) {
                apiRespone.setResult(newProduct.encodePrettily());
                apiRespone.setNumber(STATUS_OK);
            } else {
                apiRespone.setResult(MESSAGE_FAIL);
                apiRespone.setNumber(STATUS_FAIL);
            }
            apiRespone.responeHandle(ctx);
        });
    }

    public List<SmartPhone> getAll(MongoClient mongo, RoutingContext routingContext) {
        APIRespone apiRespone = new APIRespone();
        JsonObject query = new JsonObject();
        List<SmartPhone> list = new ArrayList<>();
        mongo.find(COLLECTION_NAME, query, res -> {
            if (res.succeeded()) {
                for (JsonObject json : res.result()) {
                    System.out.println(json.encodePrettily());
                    String id = json.getString("_id");
                    String name = json.getString("name");
                    String description = json.getString("description");
                    String price = json.getString("price");
                    SmartPhone smartPhone = new SmartPhone(id, name, description, price);
                    list.add(smartPhone);
                }
                apiRespone.setResult(list.toString());
                apiRespone.setNumber(STATUS_OK);

            } else {
                apiRespone.setResult(MESSAGE_FAIL);
                apiRespone.setNumber(STATUS_FAIL);
            }
            apiRespone.responeHandle(routingContext);
        });

        return null;
    }

    public void update(MongoClient mongo, RoutingContext routingContext) {
        APIRespone apiRespone = new APIRespone();
        JsonObject requestBody = routingContext.getBodyAsJson();
        JsonObject query = requestBody.getJsonObject("query");
        JsonObject updateFields = requestBody.getJsonObject("update");
        JsonObject update = new JsonObject().put("$set", updateFields);
        mongo.updateCollection(COLLECTION_NAME, query, update, req -> {
            if (req.succeeded()) {
                apiRespone.setResult(MESSAGE_SUCCESS);
                apiRespone.setNumber(STATUS_OK);
            } else {
                apiRespone.setResult(MESSAGE_FAIL);
                apiRespone.setNumber(STATUS_FAIL);
            }
            apiRespone.responeHandle(routingContext);
        });
    }

    public void delete(MongoClient mongo, RoutingContext routingContext) {
        APIRespone apiRespone = new APIRespone();
        JsonObject query = routingContext.getBodyAsJson();
        System.out.println(query);
        mongo.findOneAndDelete(COLLECTION_NAME, query, req -> {
            if (req.succeeded()) {
                apiRespone.setResult(MESSAGE_SUCCESS);
                apiRespone.setNumber(STATUS_OK);
            } else {
                apiRespone.setResult(MESSAGE_FAIL);
                apiRespone.setNumber(STATUS_FAIL);
            }
            apiRespone.responeHandle(routingContext);
        });
    }


}
