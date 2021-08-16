import io.vertx.core.Vertx;
import vertical.MainVertical;


public class App {

    public static void main(String[]args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVertical());
    }
}
