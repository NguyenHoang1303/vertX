package activemq;

import constants.Constants;
import io.vertx.ext.web.RoutingContext;
import org.apache.activemq.ActiveMQConnectionFactory;
import util.APIRespone;
import util.ActiveMQConnect;

import javax.jms.*;

public class Consumer {

    public static void receiveMessage(RoutingContext routingContext) {
        // Establish a connection for the consumer.
        // Note: Consumers should not use PooledConnectionFactory.
        ActiveMQConnectionFactory connectionFactory = ActiveMQConnect.createActiveMQConnectionFactory();
        APIRespone apiRespone = new APIRespone();
        try {
            Connection consumerConnection = connectionFactory.createConnection();
            consumerConnection.start();

            // Create a session.
            final Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a queue named "MyQueue".
            final Destination consumerDestination = consumerSession.createQueue(Constants.ACTIVE_MQ_QUEUE);

            // Create a message consumer from the session to the queue.
            final MessageConsumer consumer = consumerSession.createConsumer(consumerDestination);

            // Begin to wait for messages.
            final Message consumerMessage = consumer.receive(Constants.ACTIVE_MQ_TIME_OUT);

            // Receive the message when it arrives.
            final TextMessage consumerTextMessage = (TextMessage) consumerMessage;
            System.out.println("Message received: " + consumerTextMessage.getText());
            apiRespone.setResult(consumerTextMessage.getText());
            apiRespone.setNumber(Constants.STATUS_OK);

            // Clean up the consumer.
            consumer.close();
            consumerSession.close();
            consumerConnection.close();
        } catch (JMSException e) {
            apiRespone.setResult(Constants.MESSAGE_FAIL);
            apiRespone.setNumber(Constants.STATUS_FAIL);
            e.printStackTrace();
        }
        apiRespone.responeHandle(routingContext);
    }
}
