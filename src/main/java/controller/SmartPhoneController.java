package controller;

import constants.Constants;
import entity.SmartPhone;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import model.SmartPhoneModel;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.APIRespone;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SmartPhoneController extends AbstractVerticle {

    public void deleteById(RoutingContext context) {
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

    public void updateById(RoutingContext context) {
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



    public void save(RoutingContext context) {
        APIRespone apiRespone = new APIRespone(Constants.MESSAGE_FAIL, Constants.STATUS_FAIL);
        JsonObject object = context.getBodyAsJson();
        Document document = Document.parse(object.toString());
        SmartPhoneModel smartPhoneModel = new SmartPhoneModel();
        smartPhoneModel.save(document);
        apiRespone.setResult(document.toJson());
        apiRespone.setNumber(Constants.STATUS_OK);
        apiRespone.responeHandle(context);

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
