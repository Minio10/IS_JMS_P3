package system;

import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import common.User;

public class Client {
	
	private ConnectionFactory connectionFactory;
	private Destination destination;
	public Client() throws NamingException
	{
		
		
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/topic/playTopic");

	}
	
	
	private String send(String text)
	{
		String str = "";
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

	public static void main(String[] args) throws NamingException {
		// TODO Auto-generated method stub
		int option;
		String temp,title,aux;
		
		Scanner in = new Scanner(System.in); 
		Client admin = new Client();
		
		

		// Register
		while(true) {
			String username = "";
			String password = "";
			System.out.println("1 - Register\n2 - Login");
			option = in.nextInt();
			
			// 1 - Register
			if(option == 1) {
				System.out.println("Choose an username to register: ");
				while(username.equals("")) {
					username = in.nextLine(); 
				}
		        
		        System.out.println("Choose a password to register: ");
		        while(password.equals("")) {
					password = in.nextLine(); 
				}
				 temp = admin.send("1 "+ username + " "+ password);
				String tokens [] = temp.split(" ");
			}
			
			// 2- Login
			else if(option == 2) {
				
				System.out.println("Username to Login: ");
				while(username.equals("")) {
					username = in.nextLine(); 
				}		        
				System.out.println("Password to Login: ");
				while(password.equals("")) {
					password = in.nextLine(); 
				} 
	
				 temp = admin.send("2 "+ username + " "+ password);
				String tokens [] = temp.split(" ");
				
				if(tokens[0].equals("SUCCESS"))
					break;
			}
			
		}
		
		
		// Other options after Register and Login
		
		
		while(option != 0) {
			title = "";
			aux = "";
			System.out.println("Choose an action:\n 3-See All Publications \n "
					+ "4-Search a publication title  \n 5-Add New Publication \n 6-Update Publication \n "
					+ "7- Remove Publication \n"
					+ " 0-Exit");
			option = in.nextInt();

			switch(option) {
			
			// See All Publications
			case 3:	
				temp = admin.send(Integer.toString(option));
				break;
				
			// Search a Publication data using a title
			case 4:
				System.out.println("Choose a publication title to search:");
				while(title.equals("")) {
					title = in.nextLine(); 
				}
				aux = "4 ";
				aux += title;
				temp = admin.send(aux);
				break;
				
			// Choose a publication to delete
			case 7:
				System.out.println("Choose a publication title to delete:");
				while(title.equals("")) {
					title = in.nextLine(); 
				}
				aux = "7 ";
				aux += title;
				temp = admin.send(aux);
				break;
				
			
			}
			
			
			
			
		}
		
		

	}

}
