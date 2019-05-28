package com.sachin.jms.messagstructure;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageExpirationDemo {

	public static void main(String[] args) throws Exception {
		InitialContext initialContext = new InitialContext();
		Queue queue = (Queue) initialContext.lookup("queue/myQueue");
		Queue expirtyQueue = (Queue) initialContext.lookup("queue/expiryQueue");
		
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
		JMSContext context = cf.createContext();
		
		JMSProducer producer = context.createProducer();
		producer.setTimeToLive(2000);
		producer.send(queue, "Message 1");
		Thread.sleep(5000);
		
		Message message = context.createConsumer(queue).receive(5000);
		System.out.println("Message Received : " + message);
		
		JMSConsumer consumer = context.createConsumer(expirtyQueue);
		System.out.println("Expired message is : " + consumer.receiveBody(String.class));
	}

}
