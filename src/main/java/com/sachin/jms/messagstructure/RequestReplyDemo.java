package com.sachin.jms.messagstructure;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class RequestReplyDemo {

	public static void main(String[] args) throws Exception {
		InitialContext initialContext = new InitialContext();
		Queue queue = (Queue) initialContext.lookup("queue/myQueue");
		Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
		
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
		JMSContext context = cf.createContext();
		
		JMSProducer producer = context.createProducer();
		producer.send(queue, "Request sent");
		JMSConsumer consumer = context.createConsumer(queue);
		String message = consumer.receiveBody(String.class);
		System.out.println("Message Received : " + message);
		
		JMSProducer replyProducer = context.createProducer();
		replyProducer.send(replyQueue, "Response sent");
		JMSConsumer replyConsumer = context.createConsumer(replyQueue);
		String replyMessage = replyConsumer.receiveBody(String.class);
		System.out.println("Message Received : " + replyMessage);
		
		
	}

}
