package activemq;

import constants.ConstantsVertX;
import io.vertx.ext.web.RoutingContext;
import org.apache.activemq.ActiveMQConnectionFactory;
import util.APIRespone;

import javax.jms.*;

public class Consumer {
    public static final String ACTIVE_MQ_QUEUE = ConstantsVertX.ACTIVE_MQ_QUEUE;
    public static final int ACTIVE_MQ_TIME_OUT = ConstantsVertX.ACTIVE_MQ_TIME_OUT;
    public static final int STATUS_FAIL = ConstantsVertX.STATUS_FAIL;
    public static final int STATUS_OK = ConstantsVertX.STATUS_OK;
    public static final String MESSAGE_FAIL = ConstantsVertX.MESSAGE_FAIL;

    public static void receiveMessage(ActiveMQConnectionFactory connectionFactory, RoutingContext routingContext) {
        // Establish a connection for the consumer.
        // Note: Consumers should not use PooledConnectionFactory.
        final Connection consumerConnection;
        APIRespone apiRespone = new APIRespone();
        try {
            consumerConnection = connectionFactory.createConnection();
            consumerConnection.start();

            // Create a session.
            final Session consumerSession = consumerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a queue named "MyQueue".
            final Destination consumerDestination = consumerSession.createQueue(ACTIVE_MQ_QUEUE);

            // Create a message consumer from the session to the queue.
            final MessageConsumer consumer = consumerSession.createConsumer(consumerDestination);

            // Begin to wait for messages.
            final Message consumerMessage = consumer.receive(ACTIVE_MQ_TIME_OUT);

            // Receive the message when it arrives.
            final TextMessage consumerTextMessage = (TextMessage) consumerMessage;
            System.out.println("Message received: " + consumerTextMessage.getText());
            apiRespone.setResult("Message dequeued");
            apiRespone.setNumber(STATUS_OK);
            // Clean up the consumer.
            consumer.close();
            consumerSession.close();
            consumerConnection.close();
        } catch (JMSException e) {
            apiRespone.setResult(MESSAGE_FAIL);
            apiRespone.setNumber(STATUS_FAIL);
            e.printStackTrace();
        }
        apiRespone.responeHandle(routingContext);
    }
}
