package controller;

import activemq.ActiveMQConnect;
import activemq.Consumer;
import activemq.Producer;
import constants.ConstantsVertX;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import util.ConnectMongoDB;
import model.MongoCRUD;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MainVertical extends AbstractVerticle {
    private static final int SERVER_PORT = ConstantsVertX.SERVER_PORT;
    private static final String APPLICATION_JSON = ConstantsVertX.APPLICATION_JSON;

    @Override
    public void start() {
        //conncet Mongo
//        MongoClient mongoClient = ConnectMongoDB.connectMongoClient(vertx);

        //conncet ACtiveMQ
        ActiveMQConnectionFactory activeMQConnection = ActiveMQConnect.createActiveMQConnectionFactory();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()).produces(APPLICATION_JSON);
//        router.post("/products/save").handler( ctx -> {
//            JsonObject product = ctx.getBodyAsJson();
//            MongoCRUD.save(mongoClient, ctx);
//        });
//        router.get("/products").handler( ctx -> MongoCRUD.getAll(mongoClient, ctx));
//        router.put("/products/update").handler( ctx -> MongoCRUD.update(mongoClient, ctx));
//        router.delete("/products/delete").handler( ctx -> MongoCRUD.delete(mongoClient, ctx));
//        router.post("/message/enqueue").handler( ctx -> Producer.sendMessage(activeMQConnection, ctx));
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