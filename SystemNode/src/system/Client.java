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
			System.out.println("\n\n\n\n\n");
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
		int option,opt;
		String temp,title,aux,date,type,newtitle;
		
		Scanner in = new Scanner(System.in); 
		Client admin = new Client();
		
		// Thread receiving notifications from admin
		Thread notif = new Thread(new ClientThread());
		notif.start();
		
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
			newtitle="";
			aux = "";
			date = "";
			type = "";
			System.out.println("Choose an action:\n 3-See All Publications \n "
					+ "4-Search a publication title  \n 5-Add New Publication \n 6-Update Publication title \n "
					+ "7-Remove Publication \n 20-Update Publication date\n 21-Update Publication type\n "
					+ "0-Exit");
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
			
				
			//Add a new publication	
			case 5:
				aux = "5 ";
				System.out.println("Insert title of publication: ");
				while(title.equals("")) {
					title=in.nextLine();
				}
				aux+=title + " ";
				System.out.println("Insert publication date (dd/mm/yyyy): ");
				while(date.equals("")) {
					date=in.nextLine();
				}
				aux+=date+" ";
				System.out.println("Insert publication type: ");
				while(type.equals("")) {
					type=in.nextLine();
				}
				aux+=type;
				temp=admin.send(aux);
				break;
				
			//Update a Publication title
			case 6:
				aux="6 ";
				System.out.println("Insert title of publication: ");
				while(title.equals("")) {
					title=in.nextLine();
				}
				aux+=title + " ";
				System.out.println("Insert updated title of publication: ");
				while(newtitle.equals("")) {
					newtitle=in.nextLine();
				}
				aux+=newtitle;
				temp=admin.send(aux);
				break;
				
			//Update Publication date
			case 20:
				aux="20 ";
				System.out.println("Insert title of publication: ");
				while(title.equals("")) {
					title=in.nextLine();
				}
				aux+=title + " ";
				System.out.println("Insert updated date of publication: ");
				while(newtitle.equals("")) {
					newtitle=in.nextLine();
				}
				aux+=newtitle;
				temp=admin.send(aux);
				break;
				
			//Update Publication type	
			case 21:
				aux="21 ";
				System.out.println("Insert title of publication: ");
				while(title.equals("")) {
					title=in.nextLine();
				}
				aux+=title + " ";
				System.out.println("Insert updated type of publication: ");
				while(newtitle.equals("")) {
					newtitle=in.nextLine();
				}
				aux+=newtitle;
				temp=admin.send(aux);
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
