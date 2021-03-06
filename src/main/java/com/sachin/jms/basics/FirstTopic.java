package com.sachin.jms.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class FirstTopic {

	public static void main(String[] args) throws Exception {
		InitialContext initialContext = new InitialContext();
		Topic topic = (Topic) initialContext.lookup("topic/myTopic");
		
		ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
		Connection con = cf.createConnection();
		
		Session session = con.createSession();
		MessageProducer producer = session.createProducer(topic);
		
		MessageConsumer consumer1 = session.createConsumer(topic);
		MessageConsumer consumer2 = session.createConsumer(topic);
		TextMessage textMessage = session.createTextMessage("I am the creator of this topic to which you are listening to");
		
		producer.send(textMessage);
		
		con.start();
		
		TextMessage messageReceived1 = (TextMessage) consumer1.receive(1000);
		System.out.println("Message received by consumer 1 : " + messageReceived1.getText());
		TextMessage messageReceived2 = (TextMessage) consumer2.receive(1000);
		System.out.println("Message received by consumer 2 : " + messageReceived2.getText());
		
		session.close();
		con.close();
		initialContext.close();
	}

}
