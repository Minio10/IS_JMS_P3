package system;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClientThread implements Runnable{
	
	private ConnectionFactory connectionFactory;
	private Destination destination;
	
	public ClientThread() throws NamingException {
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/topic/playCalmo");
	}
	
	public void run() {
		
		String msg = null;
		//			try (JMSContext context = connectionFactory.createContext("calmo", "Calmo@1997");)

		try (JMSContext context = connectionFactory.createContext("calmo", "Calmo@1997");){
			
			JMSConsumer mc = context.createConsumer(destination);
			while(true) {
				
				msg = mc.receiveBody(String.class);
				System.out.println("Notification from admin: "+msg);
			}
			
				
		}		
			catch(JMSRuntimeException re) {
				
				re.printStackTrace();
			}	
		
	}
		
				

		
	}


