package activemq;

import constants.ConstantsVertX;
import io.vertx.ext.web.RoutingContext;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import util.APIRespone;
import util.ActiveMQConnect;

import javax.jms.*;

public class Producer {

    public static final String ACTIVE_MQ_QUEUE = ConstantsVertX.ACTIVE_MQ_QUEUE;
    public static final String MESSAGE_FAIL = ConstantsVertX.MESSAGE_FAIL;
    public static final int STATUS_FAIL = ConstantsVertX.STATUS_FAIL;
    public static final int STATUS_OK = ConstantsVertX.STATUS_OK;

    public static void sendMessage(ActiveMQConnectionFactory activeMQConnection, RoutingContext routingContext) {
        // Establish a connection for the producer.
        final PooledConnectionFactory producerConnectionFactory = ActiveMQConnect.createPooledConnectionFactory(activeMQConnection);
        APIRespone apiRespone = new APIRespone();
        System.out.println("hello");
        try {
            Connection producerConnection;
            producerConnection = producerConnectionFactory.createConnection();
            producerConnection.start();
            // Create a session.
            final Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Create a queue named "MyQueue".
            final Destination producerDestination = producerSession.createQueue(ACTIVE_MQ_QUEUE);
            // Create a producer from the session to the queue.
            final MessageProducer producer = producerSession.createProducer(producerDestination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // Create a message.
            final String text = routingContext.getBody().toString();
            final TextMessage producerMessage = producerSession.createTextMessage(text);
            // Send the message.
            producer.send(producerMessage);
            System.out.println("Message sent.");
            apiRespone.setResult("Sent message " + ACTIVE_MQ_QUEUE + "success!!");
            apiRespone.setNumber(STATUS_OK);
            // Clean up the producer.
            producer.close();
            producerSession.close();
            producerConnection.close();
        } catch (JMSException e) {
            System.out.println("sent message fail");
            apiRespone.setResult(MESSAGE_FAIL);
            apiRespone.setNumber(STATUS_FAIL);
            e.printStackTrace();
        }
        apiRespone.responeHandle(routingContext);
    }
}
