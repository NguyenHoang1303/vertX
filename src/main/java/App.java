import io.vertx.core.Vertx;
import vertical.MainVertical;
import vertical.MainVerticalSmartPhone;


public class App {

    public static void main(String[]args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVertical());
//        vertx.deployVerticle(new MainVerticalSmartPhone());
    }
}
