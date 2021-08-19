package controller;

import activemq.Consumer;
import activemq.Producer;
import constants.Constants;
import entity.SmartPhone;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import model.SmartPhoneModel;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.APIRespone;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SmartPhoneController extends AbstractVerticle {

    @Override
    public void start() {

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()).produces(Constants.APPLICATION_JSON);
        router.post("/products/save").handler(context -> {
            APIRespone apiRespone = new APIRespone(Constants.MESSAGE_FAIL, Constants.STATUS_FAIL);
            JsonObject object = context.getBodyAsJson();
            Document document = Document.parse(object.toString());
            save(document);
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(document.toJson());
        });
        router.get("/products").handler(this::getAll);
        router.put("/products/update").handler(this::updateById);
        router.delete("/products/delete").handler(this::deleteById);
        router.post("/message/enqueue").handler(Producer::sendMessage);
        router.post("/message/dequeue").handler(Consumer::receiveMessage);

        vertx.createHttpServer().requestHandler(router)
                .listen(Constants.SERVER_PORT, rs -> {
                    if (rs.succeeded()) {
                        System.out.println("HTTP sever port " + Constants.SERVER_PORT);
                    } else {
                        System.err.println("cloud not start Http server: " + rs.cause());
                    }
                });
    }

    @Override
    public void stop() {
        System.err.println("Shutting down");
    }

    private void deleteById(RoutingContext context) {
        APIRespone apiRespone = new APIRespone(Constants.MESSAGE_FAIL, Constants.STATUS_FAIL);
        JsonObject object = context.getBodyAsJson();
        String id = object.getString("_id");
        Document queryId = new Document();
        queryId.append("_id", new ObjectId(id));
        SmartPhoneModel smartPhoneModel = new SmartPhoneModel();
        smartPhoneModel.delete(queryId);
        apiRespone.setResult(Constants.MESSAGE_SUCCESS);
        apiRespone.setNumber(Constants.STATUS_OK);
        apiRespone.responeHandle(context);
    }

    private void updateById(RoutingContext context) {
        APIRespone apiRespone = new APIRespone(Constants.MESSAGE_FAIL, Constants.STATUS_FAIL);
        JsonObject object = context.getBodyAsJson();
        String id = object.getString("_id");
        Document queryId = new Document();
        queryId.append("_id", new ObjectId(id));
        Document updateDocument = new Document();
        Set<String> listKey = object.fieldNames();
        for (String key: listKey) {
            if (key.equals("_id")) continue;
            updateDocument.append(key,object.getString(key));
        }
        SmartPhoneModel smartPhoneModel = new SmartPhoneModel();
        smartPhoneModel.updateById(queryId, updateDocument);
        apiRespone.setNumber(Constants.STATUS_OK);
        apiRespone.setResult(Constants.MESSAGE_SUCCESS);
        apiRespone.responeHandle(context);
    }



    public void save(Document document) {
        SmartPhoneModel smartPhoneModel = new SmartPhoneModel();
        smartPhoneModel.save(document);

    }

    public void getAll(RoutingContext context) {
        APIRespone apiRespone = new APIRespone(Constants.MESSAGE_FAIL, Constants.STATUS_FAIL);
        SmartPhoneModel smartPhoneModel = new SmartPhoneModel();
        List<SmartPhone> list = smartPhoneModel.getAll();
        List<String> resultList = new ArrayList<>();
        for (SmartPhone phone : list) {
            Document document = new Document();
            document.append("_id", phone.getId())
                            .append("name", phone.getName())
                            .append("description", phone.getDesciption())
                            .append("price", phone.getPrice());
            resultList.add(document.toJson());
        }
        apiRespone.setNumber(Constants.STATUS_OK);
        apiRespone.setResult(resultList.toString());
        apiRespone.responeHandle(context);
    }
}
