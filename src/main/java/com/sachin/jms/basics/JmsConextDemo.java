package com.sachin.jms.basics;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class JmsConextDemo {

	public static void main(String[] args) throws Exception {
		InitialContext initialContext = new InitialContext();
		Queue queue = (Queue) initialContext.lookup("queue/myQueue");
		
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
		JMSContext context = cf.createContext();
		
		context.createProducer().send(queue, "Message 1");
		String message = context.createConsumer(queue).receiveBody(String.class);
		System.out.println("Message Received : " + message);
	}

}
