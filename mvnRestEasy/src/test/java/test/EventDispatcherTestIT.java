 package test;

import java.util.Properties;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.junit.Test;

public class EventDispatcherTestIT extends EventDispatcher {
	private static final String CONTEXT_ID = "12345678-02";
	private static final String CONTEXT_TYPE = "requestSignatureWorkflow";
    private static final String EMAIL = "francis.delacruz@uspto.gov";

    @Test
    public void testSetupQueueConnection() {
        try {
		MapMessage message = getSession().createMapMessage();
		message.setString("contextType", CONTEXT_TYPE);
		message.setString("contextId", CONTEXT_ID);

		message.setString("office_action_id", CONTEXT_ID);


		publisher.send(message);
		System.out.println("Start RequestSignatureWorkflow message sent!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

