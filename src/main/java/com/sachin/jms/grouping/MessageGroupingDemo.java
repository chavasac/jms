package com.sachin.jms.grouping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageGroupingDemo {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext context = new InitialContext();
		Queue requestQueue = (Queue) context.lookup("queue/myQueue");

		Map<String, String> receivedMessages = new ConcurrentHashMap<String, String>();

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		JMSContext jmsContext = connectionFactory.createContext();

		JMSContext jmsContext2 = connectionFactory.createContext();
		JMSConsumer consumer = jmsContext2.createConsumer(requestQueue);
		consumer.setMessageListener(new MyListener("Consumer-1", receivedMessages));
		JMSConsumer consumer2 = jmsContext2.createConsumer(requestQueue);
		consumer2.setMessageListener(new MyListener("Consumer-2", receivedMessages));

		JMSProducer producer = jmsContext.createProducer();

		int msgs = 10;

		TextMessage[] messages = new TextMessage[msgs];
		for (int i = 0; i < msgs; i++) {
			messages[i] = jmsContext.createTextMessage("Group-0 message" + i);
			messages[i].setStringProperty("JMSXGroupID", "Group-0");
			producer.send(requestQueue, messages[i]);
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (TextMessage message : messages) {
			if (!receivedMessages.get(message.getText()).equals("Consumer-1")) {
				throw new IllegalStateException("Group message : " + message.getText() + " has gone to the wrong receiver");
			}
		}

	}

}
