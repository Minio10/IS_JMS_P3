package system;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Notif implements Runnable{
	
		
		private ConnectionFactory connectionFactory;
		private Destination destination;
		public String text;
		
		public Notif(String text) throws NamingException {
			this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
			this.destination = InitialContext.doLookup("jms/topic/playCalmo");
			this.text = text;
		}
		
		public void run() {
			//			try (JMSContext context = connectionFactory.createContext("calmo", "Calmo@1997");)

			try (JMSContext context = connectionFactory.createContext("calmo", "Calmo@1997");){
				
				JMSProducer messageProducer = context.createProducer();
				messageProducer.send(destination, text);
				
			}
			
			
		}

}
