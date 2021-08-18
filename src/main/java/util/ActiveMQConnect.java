package util;

import constants.ConstantsVertX;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

public class ActiveMQConnect {
    private static final String ACTIVE_MQ_USERNAME = ConstantsVertX.ACTIVE_MQ_USERNAME;
    private static final String ACTIVE_MQ_PASSWORD = ConstantsVertX.ACTIVE_MQ_PASSWORD;
    private static final String ACTIVE_MQ_URL = ConstantsVertX.ACTIVE_MQ_URL;
    private static final int ACTIVE_MQ_MAXCONNECTION = ConstantsVertX.ACTIVE_MQ_MAXCONNECTION;

    public static ActiveMQConnectionFactory createActiveMQConnectionFactory() {
        // Create a connection factory.
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        // Pass the username and password.
        connectionFactory.setUserName(ACTIVE_MQ_USERNAME);
        connectionFactory.setPassword(ACTIVE_MQ_PASSWORD);
        connectionFactory.setBrokerURL(ACTIVE_MQ_URL);
        return connectionFactory;
    }

    public static PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
        // Create a pooled connection factory.
        final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(ACTIVE_MQ_MAXCONNECTION);
        return pooledConnectionFactory;
    }
}
