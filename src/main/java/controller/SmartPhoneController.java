package controller;

import util.ActiveMQConnect;
import activemq.Consumer;
import activemq.Producer;
import constants.ConstantsVertX;
import entity.SmartPhone;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import model.SmartPhoneModel;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.APIRespone;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SmartPhoneController extends AbstractVerticle {

    private static final int SERVER_PORT = ConstantsVertX.SERVER_PORT;
    private static final String APPLICATION_JSON = ConstantsVertX.APPLICATION_JSON;
    private static final String MESSAGE_FAIL = ConstantsVertX.MESSAGE_FAIL;
    private static final String MESSAGE_SUCCESS = ConstantsVertX.MESSAGE_SUCCESS;
    private static final int STATUS_OK = ConstantsVertX.STATUS_OK;
    private static final int STATUS_FAIL = ConstantsVertX.STATUS_FAIL;

    @Override
    public void start() {
        //conncet ACtiveMQ
        ActiveMQConnectionFactory activeMQConnection = ActiveMQConnect.createActiveMQConnectionFactory();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()).produces(APPLICATION_JSON);
        router.post("/products/save").handler(this::save);
        router.get("/products").handler(this::getAll);
        router.put("/products/update").handler(this::updateById);
        router.delete("/products/delete").handler(this::deleteById);
        router.post("/message/enqueue").handler(ctx -> Producer.sendMessage(activeMQConnection, ctx));
        router.post("/message/dequeue").handler(ctx -> Consumer.receiveMessage(activeMQConnection, ctx));

        vertx.createHttpServer().requestHandler(router)
                .listen(SERVER_PORT, rs -> {
                    if (rs.succeeded()) {
                        System.out.println("HTTP sever port " + SERVER_PORT);
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
        APIRespone apiRespone = new APIRespone(MESSAGE_FAIL, STATUS_FAIL);
        JsonObject object = context.getBodyAsJson();
        String id = object.getString("_id");
        Document queryId = new Document();
        queryId.append("_id", new ObjectId(id));
        SmartPhoneModel smartPhoneModel = new SmartPhoneModel();
        smartPhoneModel.delete(queryId);
        apiRespone.setResult(MESSAGE_SUCCESS);
        apiRespone.setNumber(STATUS_OK);
        apiRespone.responeHandle(context);
    }

    private void updateById(RoutingContext context) {
        APIRespone apiRespone = new APIRespone(MESSAGE_FAIL, STATUS_FAIL);
        JsonObject object = context.getBodyAsJson();
        String id = object.getString("_id");
        Document queryId = new Document();
        queryId.append("_id", new ObjectId(id));
        Document updateDocument = new Document();
        Set<String> listKey = object.fieldNames();
        for (String key: listKey) {
            if (key.equals("_id")) break;
            updateDocument.append(key,object.getString(key));
        }
        SmartPhoneModel smartPhoneModel = new SmartPhoneModel();
        smartPhoneModel.updateById(queryId, updateDocument);
        apiRespone.setNumber(STATUS_OK);
        apiRespone.setResult(MESSAGE_SUCCESS);
        apiRespone.responeHandle(context);
    }



    public void save(RoutingContext context) {
        APIRespone apiRespone = new APIRespone(MESSAGE_FAIL, STATUS_FAIL);
        JsonObject object = context.getBodyAsJson();
        Document document = new Document();
        Set<String> list = object.fieldNames();
        for (String setName: list) {
            document.append(setName,object.getString(setName));
        }
        SmartPhoneModel smartPhoneModel = new SmartPhoneModel();
        smartPhoneModel.save(document);
        apiRespone.setNumber(STATUS_OK);
        apiRespone.setResult(MESSAGE_SUCCESS);
        apiRespone.responeHandle(context);
    }

    public void getAll(RoutingContext context) {
        APIRespone apiRespone = new APIRespone(MESSAGE_FAIL, STATUS_FAIL);
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
        apiRespone.setNumber(STATUS_OK);
        apiRespone.setResult(resultList.toString());
        apiRespone.responeHandle(context);
    }
}
