import controller.SmartPhoneController;
import io.vertx.core.Vertx;
import controller.MainVertical;


public class App {

    public static void main(String[]args) {
        Vertx vertx = Vertx.vertx();
//        vertx.deployVerticle(new MainVertical());
        vertx.deployVerticle(new SmartPhoneController());
    }
}
