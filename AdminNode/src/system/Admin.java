package system;

import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Admin {
	
	private ConnectionFactory connectionFactory;
	private Destination destination;
	public Admin() throws NamingException
	{
		
		
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/topic/playTopic");

	}
	
	
	private String send(String text)
	{
		String str = "";
		// try (JMSContext context = connectionFactory.createContext("calmo", "Calmo@1997");)
		try (JMSContext context = connectionFactory.createContext("calmo", "Calmo@1997");)
		{
			JMSProducer messageProducer = context.createProducer();
			TextMessage msg = context.createTextMessage();
			Destination tmp = context.createTemporaryQueue();
			msg.setJMSReplyTo(tmp);
			msg.setText(text);
			messageProducer.send(destination, msg);
			
			JMSConsumer cons = context.createConsumer(tmp);
			str = cons.receiveBody(String.class);
			System.out.println("System: "+ str);
		}
		catch (Exception re)
		{
			re.printStackTrace();
		}
		return str;


	}

	public static void main(String[] args) throws NamingException{
		// TODO Auto-generated method stub
		int option = 9999;
		String temp;
		Scanner in = new Scanner(System.in); 
		Admin admin = new Admin();
		
		while(option != 0) {
			System.out.println("Choose an action:\n8-List all Users \n "
					+ "9-List all Pending Tasks  \n10-Deactivate User \n 11-List All Publications \n "
					+ "12- Publication Information \n"
					+ " 0-Exit");

			option = in.nextInt();
			temp = admin.send(Integer.toString(option));
				
		}
		

	}

}
