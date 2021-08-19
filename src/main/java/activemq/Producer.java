package activemq;

import constants.Constants;
import io.vertx.ext.web.RoutingContext;
import org.apache.activemq.ActiveMQConnectionFactory;
import util.APIRespone;
import util.ActiveMQConnect;

import javax.jms.*;

public class Producer {

    public static void sendMessage(RoutingContext routingContext) {
        // Establish a connection for the producer.
        ActiveMQConnectionFactory activeMQConnectionFactory = ActiveMQConnect.createActiveMQConnectionFactory();
        APIRespone apiRespone = new APIRespone();
        try {
            Connection producerConnection = activeMQConnectionFactory.createConnection();
            producerConnection.start();
            // Create a session.
            Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a queue named "MyQueue".
            Destination producerDestination = producerSession.createQueue(Constants.ACTIVE_MQ_QUEUE);

            // Create a producer from the session to the queue.
            MessageProducer producer = producerSession.createProducer(producerDestination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a message.
            String text = routingContext.getBody().toString();
            TextMessage producerMessage = producerSession.createTextMessage(text);

            // Send the message.
            producer.send(producerMessage);
            System.out.println("Message sent.");
            apiRespone.setResult("Sent message " + Constants.ACTIVE_MQ_QUEUE + "success!!");
            apiRespone.setNumber(Constants.STATUS_OK);

            // Clean up the producer.
            producer.close();
            producerSession.close();
            producerConnection.close();
        } catch (JMSException e) {
            System.out.println("sent message fail");
            apiRespone.setResult(Constants.MESSAGE_FAIL);
            apiRespone.setNumber(Constants.STATUS_FAIL);
            e.printStackTrace();
        }
        apiRespone.responeHandle(routingContext);
    }
}
