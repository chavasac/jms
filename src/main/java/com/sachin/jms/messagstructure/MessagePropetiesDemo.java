package com.sachin.jms.messagstructure;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessagePropetiesDemo {

	public static void main(String[] args) throws Exception {
		InitialContext initialContext = new InitialContext();
		Queue queue = (Queue) initialContext.lookup("queue/myQueue");

		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
		JMSContext context = cf.createContext();

		JMSProducer producer = context.createProducer();
		TextMessage message = context.createTextMessage("Message 1");
		//Setting custom properties
		message.setBooleanProperty("loggedIn", true);
		message.setStringProperty("user", "abc123");
		producer.send(queue, message);
		
		TextMessage receivedMessage = (TextMessage) context.createConsumer(queue).receive();
		System.out.println("Message Received : " + receivedMessage);
		System.out.println("Message property : loggedIn = " + receivedMessage.getBooleanProperty("loggedIn"));
		System.out.println("Message property : user = " + receivedMessage.getStringProperty("user"));
	}

}
