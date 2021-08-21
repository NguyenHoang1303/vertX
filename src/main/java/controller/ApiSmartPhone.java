package controller;

import activemq.Consumer;
import activemq.Producer;
import constants.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class ApiSmartPhone extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        SmartPhoneController smartPhoneController = new SmartPhoneController();
        router.route().handler(BodyHandler.create()).produces(Constants.APPLICATION_JSON);
        router.post("/products/save").handler(smartPhoneController::save);
        router.get("/products").handler(smartPhoneController::getAll);
        router.put("/products/update").handler(smartPhoneController::updateById);
        router.delete("/products/delete").handler(smartPhoneController::deleteById);
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
}
