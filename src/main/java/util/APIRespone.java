package util;

import constants.Constants;
import io.vertx.ext.web.RoutingContext;

public class APIRespone {
    private String result;
    private int number;
    private static final String APPLICATION_JSON = Constants.APPLICATION_JSON;
    private static final String CONTENT_TYPE = Constants.CONTENT_TYPE;

    public void responeHandle(RoutingContext routingContext){
        routingContext.response()
                .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setStatusCode(this.number)
                .end(this.result);
    }


    public APIRespone(String result, int number) {
        this.result = result;
        this.number = number;
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
