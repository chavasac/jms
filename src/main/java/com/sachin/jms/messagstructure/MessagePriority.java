package com.sachin.jms.messagstructure;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessagePriority {

	public static void main(String[] args) throws NamingException {
		InitialContext initialContext = new InitialContext();
		Queue queue = (Queue) initialContext.lookup("queue/myQueue");
		
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
		JMSContext context = cf.createContext();
		
		String[] messages = new String[3];
		messages[0] = "Message one";
		messages[1] = "Message two";
		messages[2] = "Message three";
		
		JMSProducer producer = context.createProducer();
		producer.setPriority(4);
		producer.send(queue, messages[0]);
		
		producer.setPriority(1);
		producer.send(queue, messages[1]);
		
		producer.setPriority(9);
		producer.send(queue, messages[2]);
		
		JMSConsumer consumer = context.createConsumer(queue);
		
		for (int i = 0; i < 3; i++) {
			System.out.println(consumer.receiveBody(String.class));
		}
		

	}

}
