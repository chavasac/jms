package com.sachin.jms.basics;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueBrowserDemo {
	
	public static void main(String args[]) {
		InitialContext initialContext = null;
		Connection con = null;
		Session session = null;
		try {
			initialContext = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			con = cf.createConnection();
			session = con.createSession();
			Queue queue = (Queue) initialContext.lookup("queue/myQueue");
			MessageProducer producer = session.createProducer(queue);
			TextMessage message1 = session.createTextMessage("Message 1");
			TextMessage message2 = session.createTextMessage("Message 2");
			producer.send(message1);
			producer.send(message2);

			QueueBrowser browser = session.createBrowser(queue);
			
			Enumeration<TextMessage> enumeration = browser.getEnumeration();
			while(enumeration.hasMoreElements()) {
				TextMessage message = enumeration.nextElement();
				System.out.println("Message in queue: " + message.getText());
			}
			
			MessageConsumer consumer = session.createConsumer(queue);
			con.start();
			TextMessage messageReceived = (TextMessage) consumer.receive(5000);
			System.out.println("Message received: " + messageReceived.getText());
			TextMessage messageReceived2 = (TextMessage) consumer.receive(5000);
			System.out.println("Message received: " + messageReceived2.getText());
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
				con.close();
				initialContext.close();
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
