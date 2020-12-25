package system;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import common.User;
import common.Publication;
public class SystemNode
{
	private ConnectionFactory connectionFactory;
	private Destination destination;
	public SystemNode() throws NamingException
	{
		this.connectionFactory = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.destination = InitialContext.doLookup("jms/topic/playTopic");
	}
	
	
	// FUnction to delete a publication
	public String deletePub(String title) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();

		
		String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	for(Publication p: mylist) {
    		if(p.getName().equals(title)) {
    			em.remove(p);
    			em.getTransaction().commit();
    			em.close();
    			emf.close();
    			return "The publication was removed successfully\n";
    		}
    			
    		
    	}
    	em.close();
		emf.close();
		return "The publication wasn't removed or didnt exist!\n";
		
		
		
	}
	
	// Add Publication
	public String addPub(String title, String date,String type) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	for(Publication p: mylist) {
    		if(p.getName().equals(title)) {
    			return "This publication already exists";
    		}
    	}
		Publication pb1 = new Publication(title,type,date);
		em.persist(pb1);
		em.getTransaction().commit();
		em.close();
		emf.close();
		return "Publication added";

	}
	
	// UpdatePublication Title
	public String updateTitle(String title,String newtitle) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	for(Publication p:mylist) {
    		if(p.getName().equals(title)) {
    			p.setName(newtitle);
    			Publication pb1 = p;
    			em.remove(p);
    			em.persist(pb1);
    			em.getTransaction().commit();
    			em.close();
    			emf.close();
    			return "Publication Updated";
    		}
    	}
    	return "Publication did not exist";
    	
	}
	
	// Update Publication Type
	public String updateType(String title,String newType) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	for(Publication p:mylist) {
    		if(p.getName().equals(title)) {
    			p.setType(newType);
    			Publication pb1 = p;
    			em.remove(p);
    			em.persist(pb1);
    			em.getTransaction().commit();
    			em.close();
    			emf.close();
    			return "Publication Updated";
    		}
    	}
    	return "Publication did not exist";
	}
	
	// Update Publication PubDate
	public String updateDate(String title, String newdate) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	for(Publication p:mylist) {
    		if(p.getName().equals(title)) {
    			p.setPubDate(newdate);
    			Publication pb1=p;
    			em.remove(p);
    			em.persist(pb1);
    			em.getTransaction().commit();
    			em.close();
    			emf.close();
    			return "Publication Updated";
    		}
    	}
    	return "Publication did not exist";
	}
	
	// Function returns all info from a certain Publication title
public String titlePubInfo(String title) {
	
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
    	
    	String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	String kappa = "";
    	
    	for(Publication p: mylist) {
    		if(p.getName().equals(title)) {

        		kappa += "Name: " + p.getName() + " Type: " + p.getType() + " Publication Date: " + p.getPubDate();
    		}
    	}
    	return kappa; 	
	}
	
	// Function goes to the DB and returns all Publication Titles if they exist
	public String getPubTitles() {
		
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		    	
    	String jpql = "SELECT s FROM Publication s";
    	TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
    	List<Publication> mylist = typedQuery.getResultList();
    	
    	String temp ="";
    	// Gets all info from Publications
    	for(Publication p: mylist) {
    		temp += "Publication\n";
    		temp += "Name: " + p.getName()+"\n";	
    		System.out.println();
    		temp+="\n";
    	}
    	return temp;
     	
    }
    
	// Function that verifies if username and password match one from the DB
	public int verifyLogin(String username, String password) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		String jpql = "Select u FROM User u";
    	TypedQuery<User> typedQuery = em.createQuery(jpql, User.class);
    	List<User> listUsers = typedQuery.getResultList();
    	
    	for(User i: listUsers) {
    		if(i.getUsername().equals(username) && i.getPassword().equals(password))
    			return 0; // 0 - Username and Password match the ones found in the DB
    	}
    	
    	em.close();
		emf.close();
    	
    	return 1;  // No matches were found inside the DB
    	
    	
		
		
		
	}
	
	// ADDS A researcher to the DB
	public int addResearcher(String username,String password) {
		
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		String jpql = "Select u FROM User u";
    	TypedQuery<User> typedQuery = em.createQuery(jpql, User.class);
    	List<User> listUsers = typedQuery.getResultList();
    	
    	for(User i: listUsers) {
    		if(i.getUsername().equals(username))
    			return 1; // 1 - Username ja existe
    	}
		
		User user = new User(username,password);
		
		em.persist(user);
		
		em.getTransaction().commit();
		em.close();
		emf.close();
		
		return 0; // Username doesnt exist and the registration is completed
	}
	
	
	
	private void receive() throws JMSException, FileNotFoundException, NamingException, IOException
	{
		// try (JMSContext context = connectionFactory.createContext("calmo", "Calmo@1997");)
		try (JMSContext context = connectionFactory.createContext("calmo", "Calmo@1997");)
		{
			
			JMSConsumer consumer = context.createConsumer(destination);
			TextMessage msg = (TextMessage) consumer.receive();
			Scanner in = new Scanner(System.in); 
			String temp,opt;
			
			System.out.println("Researcher: "+ msg.getText());
			
			String [] tokens = msg.getText().split(" ");
			
			JMSProducer producer = context.createProducer();
			TextMessage reply = context.createTextMessage();
			
			// task comes from the admin and its the same as the one from Researcher
			if(tokens[0].equals("11"))
				tokens[0] = "3";
			
			switch(tokens[0]) {
				// 1 - Register. User needs admins approval 				
				case "1": 
					System.out.println("Do you want to register this Researcher: y/n ?");
			        String option = in.nextLine();
			        
			        if(option.equals("y")) {
			        	int res = addResearcher(tokens[1],tokens[2]);
			        	if(res == 0)
			        		reply.setText("SUCCESS Researcher " + tokens[1]+ " has been registered with success!");
			        	else
			        		reply.setText("The username already exists! Please try again");

			        }
			        		        
			        else
						reply.setText("ERROR Researcher " + tokens[1]+ " wasnt registered!");

					producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
					
				// Login doesnt need admin's approval
				case "2":
					if(verifyLogin(tokens[1],tokens[2])== 0)
		        		reply.setText("SUCCESS Researcher " + tokens[1]+ " has logged in !");
					else
		        		reply.setText("ERROR Researcher " + tokens[1]+ " failed to log in !");
					producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
					
				// Researcher or Admin wants to see all Publication Titles	
				case "3":
					temp = getPubTitles();
	        		reply.setText("Publications Titles: " + temp);
		        	producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
				
				// Search a Publication with a title
				case "4":
					temp = titlePubInfo(tokens[1]);
	        		reply.setText(temp);
		        	producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
					
				// Add a publication	
				case "5":
					System.out.println("Do you want add this publication: " + tokens[1]+ " y/n ?");
			         opt = in.nextLine();
			        
			        if(opt.equals("y")) {
			        	String res = addPub(tokens[1],tokens[2],tokens[3]);
			        	reply.setText(res);
			        }
			        		        
			        else
						reply.setText("ERROR Admin did not authorize");

					producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
				
				//Update publication title	
				case "6":
					System.out.println("Do you want to update this publication: "+ tokens[1]+" y/n?");
					opt = in.nextLine();
			        
			        if(opt.equals("y")) {
			        	String res = updateTitle(tokens[1],tokens[2]);
			        	reply.setText(res);
			        }
			        		        
			        else
						reply.setText("ERROR Admin did not authorize");

					producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
					
				//Update publication date
				case "20":
					System.out.println("Do you want to update this publication: "+ tokens[1]+" y/n?");
					opt = in.nextLine();
			        
			        if(opt.equals("y")) {
			        	String res = updateDate(tokens[1],tokens[2]);
			        	reply.setText(res);
			        }
			        		        
			        else
						reply.setText("ERROR Admin did not authorize");

					producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
					
				//Update publication type
				case "21":
					System.out.println("Do you want to update this publication: "+ tokens[1]+" y/n?");
					opt = in.nextLine();
			        
			        if(opt.equals("y")) {
			        	String res = updateType(tokens[1],tokens[2]);
			        	reply.setText(res);
			        }
			        		        
			        else
						reply.setText("ERROR Admin did not authorize");

					producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
				// Delete a Publication
				case "7":
					System.out.println("Do you want delete this publication: " + tokens[1]+ " y/n ?");
			         opt = in.nextLine();
			        
			        if(opt.equals("y")) {
			        	String res = deletePub(tokens[1]);
			        	reply.setText(res);
			        }
			        		        
			        else
						reply.setText("ERROR Admin did not authorize");

					producer.send(msg.getJMSReplyTo(), reply);
					System.out.println("Sent reply to " + msg.getJMSReplyTo());
					break;
					
			}
			
			
			
		}
		catch (JMSRuntimeException re)
		{
		re.printStackTrace();
		}
//		return msg;
	}
	
	public static void main(String[] args) throws NamingException, JMSException, FileNotFoundException, IOException
	{

		SystemNode client = new SystemNode();
		while(true) {
			client.receive();
		}
		
		
	}
}