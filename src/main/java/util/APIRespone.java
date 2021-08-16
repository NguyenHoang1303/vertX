package vertical;

import constants.ConstantsVertX;
import io.vertx.ext.web.RoutingContext;

public class APIRespone {
    private String result;
    private int number;
    private static final String APPLICATION_JSON = ConstantsVertX.APPLICATION_JSON;
    private static final String CONTENT_TYPE = ConstantsVertX.CONTENT_TYPE;

    public void responeHandle(RoutingContext routingContext){
        routingContext.response()
                .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setStatusCode(this.number)
                .end(this.result);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public APIRespone(String result) {
        this.result = result;
    }

    public APIRespone() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
