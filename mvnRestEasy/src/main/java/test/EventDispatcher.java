package test;

import java.util.Properties;
import java.util.Map;
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

import test.events.EventField;
import test.events.Event;

public class EventDispatcher {
	private static Context ic = null;
	private static ConnectionFactory cf = null;
	private static Connection connection = null;
	private static String destinationName = "queues/pe2e_events";
	protected static MessageProducer publisher = null;
	protected static Session session = null;

    public synchronized static void setupQueueConnection() {
        try{
            ic = getInitialContext();
            cf = (ConnectionFactory) ic.lookup("/ConnectionFactory");

            Queue queue = (Queue) ic.lookup(destinationName);

            connection = cf.createConnection();
            session    = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            publisher  = session.createProducer(queue);

            connection.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Session getSession() {
        if(session == null){
            setupQueueConnection();
        }

        return session;
    }

    public static MessageProducer getPublisher() {
        if(publisher == null){
            setupQueueConnection();
        }

        return publisher;
    }

	private static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (JMSException jmse) {
			System.out.println("Could not close connection " + con
					+ " exception was " + jmse);
		}
	}

    public static void closeQueueConnection() throws Exception {
		if (ic != null) {
			try {
				ic.close();
			} catch (Exception e) {
				throw e;
			}
		}
		closeConnection(connection);

    }

	public static Context getInitialContext() throws javax.naming.NamingException {
		Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		p.put(Context.URL_PKG_PREFIXES, " org.jboss.naming:org.jnp.interfaces");
		p.put(Context.PROVIDER_URL, "jnp://localhost:1099");

		return new javax.naming.InitialContext(p);
	}

    public static void emit(Event event) throws JMSException {
        System.out.println("Attempting to create event");
        String eventType = null;

		MapMessage message = getSession().createMapMessage();

        for(EventField field: event.getFields()) {
            if("type".equals(field.interfaceKey)){
                eventType = field.getWorkflowValue();
            }
            message
                .setString(field.workflowKey, field.getWorkflowValue());
        }

/*
 *        message.setString("contextType", event.namespace);
 *
 *        // fill in the rest of the standard event fields
 *        message.setString("contextId", event.contextId);
 *        message.setString("source", event.source);
 *        message.setString("timestamp", event.timestamp.toString());
 *
 *        // this is hacky, should be changed on the workflow side to make it
 *        // cleaner.  The next 2 lines are redundant.
 *        if(event.type == null){
 *            message.setString(event.type, "signed");
 *            message.setString("eventType", event.type);
 *        }
 *
 *        // more hacky...we should get rid of office_action_id
 *        message.setString("office_action_id", event.contextId);
 *
 *        // fill in the nonstandard event fields.
 *        Map<String,String> context = event.getContext();
 *        Set<String> names = context.keySet();
 *        for(String name: names){
 *            System.out.println(name + " " + context.get(name));
 *            message.setString(name, context.get(name));
 *        }
 */

        //done crafting message.  Now send it.
		getPublisher().send(message);
		System.out.println("Emitting Event of type: " + eventType);
    }
}
