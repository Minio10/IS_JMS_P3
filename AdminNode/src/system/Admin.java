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
		String temp,username,aux,response;
		String [] requests;
		Scanner in = new Scanner(System.in); 
		Admin admin = new Admin();
		
		while(option != 0) {
			username="";
			aux="";
			response="";
			System.out.println("Choose an action:\n8-List all Users"
					+ "\n9-List all Pending Tasks  \n10-Deactivate User \n11-List All Publications"
					+ "\n12- Publication Information \n13-Activate User"
					+ "\n0-Exit");
			
			option = in.nextInt();
			switch(option) {
				
				case 8:
				case 11:
					temp = admin.send(Integer.toString(option));
					break;
				
				
				case 9:
					temp=admin.send(Integer.toString(option));
					requests=temp.split("\n");
					System.out.println("Answer each task with y/n seperated by spaces");
					while(response.equals("")) {
						response+= in.nextLine();
					}
					temp=admin.send(response);
					break;
					
					
				case 10:
					aux="10 ";
					System.out.println("Insert the username of the user you wish to deactivate: ");
					while(username.equals("")) {
						username = in.nextLine(); 
					}
					aux+=username;
					temp = admin.send(aux);
					break;
					
				//Title is the same as username
				case 12:
					System.out.println("Choose a publication title to search:");
					while(username.equals("")) {
						username = in.nextLine(); 
					}
					aux = "12 ";
					aux += username;
					temp = admin.send(aux);
					break;
					
					
				case 13:
					aux="13 ";
					System.out.println("Insert the username of the user you wish to activate: ");
					while(username.equals("")) {
						username = in.nextLine(); 
					}
					aux+=username;
					temp = admin.send(aux);
					break;
			}	
				
		}
		

	}

}
