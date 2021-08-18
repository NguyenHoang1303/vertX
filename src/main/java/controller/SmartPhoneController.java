package controller;

import activemq.ActiveMQConnect;
import activemq.Consumer;
import activemq.Producer;
import constants.ConstantsVertX;
import io.vertx.ext.web.RoutingContext;
import model.SmartPhoneModel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import entity.SmartPhone;
import org.apache.activemq.ActiveMQConnectionFactory;
import util.ConnectMongoDB;

import java.util.List;

public class SmartPhoneController extends AbstractVerticle {
    private static final int SERVER_PORT = ConstantsVertX.SERVER_PORT;
    private static final String APPLICATION_JSON = ConstantsVertX.APPLICATION_JSON;

    @Override
    public void start() {
        //conncet ACtiveMQ
        ActiveMQConnectionFactory activeMQConnection = ActiveMQConnect.createActiveMQConnectionFactory();
        SmartPhoneModel smartPhoneModel = new SmartPhoneModel();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()).produces(APPLICATION_JSON);
        router.post("/products/save").handler(smartPhoneModel::save);
        router.get("/products").handler(smartPhoneModel::getAll);
        router.put("/products/update").handler(smartPhoneModel::update);
        router.delete("/products/delete").handler(smartPhoneModel::delete);
        router.post("/message/enqueue").handler( ctx -> Producer.sendMessage(activeMQConnection, ctx));
        router.post("/message/dequeue").handler( ctx -> Consumer.receiveMessage(activeMQConnection, ctx));

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
}
