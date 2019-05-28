package com.sachin.jms.messagstructure;

import javax.jms.BytesMessage;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageTypesDemo {

	public static void main(String[] args) throws Exception {
		InitialContext initialContext = new InitialContext();
		Queue queue = (Queue) initialContext.lookup("queue/myQueue");

		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
		JMSContext context = cf.createContext();

		JMSProducer producer = context.createProducer();
		TextMessage message = context.createTextMessage("Message 1");
		BytesMessage bytesMessage = context.createBytesMessage();
		bytesMessage.writeUTF("Sachin");
		bytesMessage.writeLong(123l);
		
		StreamMessage streamMessage = context.createStreamMessage();
		streamMessage.writeBoolean(true);
		streamMessage.writeFloat(2.5f);
		
		MapMessage mapMessage = context.createMapMessage();
		mapMessage.setBoolean("isCreditAvailable", true);
		
		ObjectMessage objectMessage = context.createObjectMessage();
		Patient patient = new Patient();
		patient.setId(123);
		patient.setName("Sachin");
		objectMessage.setObject(patient);
		
		//Send appropriate message to see the functionality of how different message types work
		producer.send(queue, objectMessage);
		
		//Uncomment below section to consume byte message
		/*
		 * BytesMessage receivedMessage = (BytesMessage)
		 * context.createConsumer(queue).receive(2000);
		 * System.out.println("Message Received : " + receivedMessage);
		 * System.out.println(receivedMessage.readUTF());
		 * System.out.println(receivedMessage.readLong());
		 */
		
		/*
		 * StreamMessage receivedMessage = (StreamMessage)
		 * context.createConsumer(queue).receive(2000);
		 * System.out.println("Message Received : " + receivedMessage);
		 * System.out.println(receivedMessage.readBoolean());
		 * System.out.println(receivedMessage.readFloat());
		 */
		
		/*
		 * MapMessage receivedMessage = (MapMessage)
		 * context.createConsumer(queue).receive(2000);
		 * System.out.println("Message Received : " + receivedMessage);
		 * System.out.println(receivedMessage.getBoolean("isCreditAvailable"));
		 */
		
		ObjectMessage receivedMessage = (ObjectMessage) context.createConsumer(queue).receive(2000);
		System.out.println("Message Received : " + receivedMessage);
		Patient patientRecived = (Patient) receivedMessage.getObject();
		System.out.println(patientRecived.getId());
		System.out.println(patientRecived.getName());
	}

}
